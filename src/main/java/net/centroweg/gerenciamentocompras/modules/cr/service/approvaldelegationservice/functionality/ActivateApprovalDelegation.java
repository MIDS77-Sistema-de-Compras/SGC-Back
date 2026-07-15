package net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.*;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.ApprovalDelegationNotFoundException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.ApprovalDelegationTransitionException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.InvalidApprovalDelegationParticipantException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.ApprovalDelegationRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality.AssignCrBranchResponsible;
import net.centroweg.gerenciamentocompras.modules.cr.service.event.ApprovalDelegationActivatedEvent;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserSummaryPublicResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivateApprovalDelegation {

    private final ApprovalDelegationRepository approvalDelegationRepository;
    private final CrBranchRepository crBranchRepository;
    private final UserPublicApi userPublicApi;
    private final ApprovalDelegationParticipantValidator participantValidator;
    private final AssignCrBranchResponsible assignCrBranchResponsible;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public ApprovalDelegation activate(Long delegationId) {
        ApprovalDelegation delegation = approvalDelegationRepository.findByIdForUpdate(delegationId)
                .orElseThrow(() -> new ApprovalDelegationNotFoundException(delegationId));

        if (delegation.getStatus() == ApprovalDelegationStatus.ACTIVE) {
            return delegation;
        }
        if (delegation.getStatus() == ApprovalDelegationStatus.FINISHED) {
            throw new ApprovalDelegationTransitionException(
                    "Uma delegação finalizada não pode ser ativada novamente."
            );
        }

        LocalDateTime now = LocalDateTime.now(clock);
        if (now.isBefore(delegation.getStartAt())) {
            throw new ApprovalDelegationTransitionException(
                    "A delegação ainda não atingiu a data de início."
            );
        }
        if (!now.isBefore(delegation.getEndAt())) {
            throw new ApprovalDelegationTransitionException(
                    "A delegação não pode ser ativada após o término do período."
            );
        }

        UserSummaryPublicResponse delegator = findParticipant(
                delegation.getDelegatorUserId(), "Supervisor titular não encontrado."
        );
        UserSummaryPublicResponse delegate = findParticipant(
                delegation.getDelegateUserId(), "Supervisor substituto não encontrado."
        );
        participantValidator.validateActiveSupervisor(delegator, "supervisor titular");
        participantValidator.validateActiveSupervisor(delegate, "supervisor substituto");

        List<Long> branchIds = branchIds(delegation);
        Map<Long, CrBranch> lockedBranches = lockBranches(branchIds);

        // A regra de limite considera apenas supervisores ativos; por isso o titular é
        // inativado antes da inclusão do substituto.
        userPublicApi.changeUserActivationStatus(delegation.getDelegatorUserId(), false);

        for (ApprovalDelegationBranch delegationBranch : delegation.getBranches()) {
            Long branchId = delegationBranch.getCrBranch().getId();
            CrBranch branch = lockedBranches.get(branchId);
            boolean alreadyResponsible = branch.getResponsibleUsers() != null
                    && branch.getResponsibleUsers().stream()
                    .anyMatch(user -> user.getId().equals(delegation.getDelegateUserId()));

            if (!alreadyResponsible) {
                assignCrBranchResponsible.assignCrBranchResponsible(
                        branchId, delegation.getDelegateUserId()
                );
            }
            boolean inheritedTemporaryRelationship = alreadyResponsible
                    && approvalDelegationRepository
                    .existsOtherActiveTemporaryDelegationForRelationship(
                            delegation.getId(),
                            ApprovalDelegationStatus.ACTIVE,
                            delegation.getDelegateUserId(),
                            branchId
                    );
            // Quando outra delegação criou o vínculo, esta também participa da limpeza.
            // Assim, a última delegação ativa o remove; vínculos permanentes continuam falsos.
            delegationBranch.setTemporaryRelationshipCreated(
                    !alreadyResponsible || inheritedTemporaryRelationship
            );
        }

        delegation.setStatus(ApprovalDelegationStatus.ACTIVE);
        delegation.setActivatedAt(now);
        ApprovalDelegation saved = approvalDelegationRepository.save(delegation);
        eventPublisher.publishEvent(new ApprovalDelegationActivatedEvent(
                saved.getId(),
                saved.getDelegatorUserId(),
                saved.getDelegateUserId(),
                List.copyOf(branchIds),
                now
        ));
        return saved;
    }

    private UserSummaryPublicResponse findParticipant(Long userId, String message) {
        return userPublicApi.findUserSummaryById(userId)
                .orElseThrow(() -> new InvalidApprovalDelegationParticipantException(message));
    }

    private List<Long> branchIds(ApprovalDelegation delegation) {
        return delegation.getBranches().stream()
                .map(branch -> branch.getCrBranch().getId())
                .sorted()
                .toList();
    }

    private Map<Long, CrBranch> lockBranches(List<Long> branchIds) {
        Map<Long, CrBranch> branches = crBranchRepository.findAllByIdForUpdate(branchIds).stream()
                .collect(Collectors.toMap(CrBranch::getId, Function.identity()));
        if (branches.size() != branchIds.size()) {
            throw new ApprovalDelegationTransitionException(
                    "Não foi possível localizar todas as filiais registradas na delegação."
            );
        }
        return branches;
    }
}
