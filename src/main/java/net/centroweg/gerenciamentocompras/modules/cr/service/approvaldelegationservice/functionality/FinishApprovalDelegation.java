package net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.*;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.ApprovalDelegationNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.ApprovalDelegationTransitionException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.ApprovalDelegationRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality.RemoveCrBranchResponsible;
import net.centroweg.gerenciamentocompras.modules.cr.service.event.ApprovalDelegationFinishedEvent;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinishApprovalDelegation {

    private final ApprovalDelegationRepository approvalDelegationRepository;
    private final CrBranchRepository crBranchRepository;
    private final UserPublicApi userPublicApi;
    private final RemoveCrBranchResponsible removeCrBranchResponsible;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public ApprovalDelegation finish(Long delegationId) {
        ApprovalDelegation delegation = approvalDelegationRepository.findByIdForUpdate(delegationId)
                .orElseThrow(() -> new ApprovalDelegationNotFoundException(delegationId));

        if (delegation.getStatus() == ApprovalDelegationStatus.FINISHED) {
            return delegation;
        }
        if (delegation.getStatus() != ApprovalDelegationStatus.ACTIVE) {
            throw new ApprovalDelegationTransitionException(
                    "Somente uma delegação ativa pode ser finalizada."
            );
        }

        LocalDateTime now = LocalDateTime.now(clock);
        if (now.isBefore(delegation.getEndAt())) {
            throw new ApprovalDelegationTransitionException(
                    "A delegação ainda não atingiu a data de término."
            );
        }

        List<Long> branchIds = delegation.getBranches().stream()
                .map(branch -> branch.getCrBranch().getId())
                .sorted()
                .toList();
        if (crBranchRepository.findAllByIdForUpdate(branchIds).size() != branchIds.size()) {
            throw new ApprovalDelegationTransitionException(
                    "Não foi possível localizar todas as filiais registradas na delegação."
            );
        }

        userPublicApi.changeUserActivationStatus(delegation.getDelegatorUserId(), true);

        for (ApprovalDelegationBranch delegationBranch : delegation.getBranches()) {
            if (!delegationBranch.isTemporaryRelationshipCreated()) {
                continue;
            }
            Long branchId = delegationBranch.getCrBranch().getId();
            boolean requiredByAnotherDelegation = approvalDelegationRepository
                    .existsOtherActiveDelegationForRelationship(
                            delegation.getId(),
                            ApprovalDelegationStatus.ACTIVE,
                            delegation.getDelegateUserId(),
                            branchId
                    );
            if (!requiredByAnotherDelegation) {
                removeCrBranchResponsible.removeCrBranchResponsible(
                        branchId, delegation.getDelegateUserId()
                );
            }
        }

        delegation.setStatus(ApprovalDelegationStatus.FINISHED);
        delegation.setFinishedAt(now);
        ApprovalDelegation saved = approvalDelegationRepository.save(delegation);
        eventPublisher.publishEvent(new ApprovalDelegationFinishedEvent(
                saved.getId(),
                saved.getDelegatorUserId(),
                saved.getDelegateUserId(),
                List.copyOf(branchIds),
                now
        ));
        return saved;
    }
}
