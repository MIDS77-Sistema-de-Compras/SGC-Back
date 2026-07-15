package net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.ApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.ApprovalDelegationStatus;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.*;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.ApprovalDelegationRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CreateApprovalDelegationRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.ApprovalDelegationResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.ApprovalDelegationMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserSummaryPublicResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateApprovalDelegation {

    private final ApprovalDelegationRepository approvalDelegationRepository;
    private final CrBranchRepository crBranchRepository;
    private final UserPublicApi userPublicApi;
    private final ApprovalDelegationParticipantValidator participantValidator;
    private final ActivateApprovalDelegation activateApprovalDelegation;
    private final ApprovalDelegationMapper mapper;
    private final Clock clock;

    @Transactional
    public ApprovalDelegationResponse create(CreateApprovalDelegationRequest request) {
        LocalDateTime now = LocalDateTime.now(clock);
        UserSummaryPublicResponse delegator = userPublicApi.getAuthenticatedUserSummary();
        participantValidator.validateActiveSupervisor(delegator, "supervisor titular");

        if (delegator.id().equals(request.delegateUserId())) {
            throw new InvalidApprovalDelegationParticipantException(
                    "Não é permitido delegar a aprovação para o próprio usuário."
            );
        }

        UserSummaryPublicResponse delegate = userPublicApi.findUserSummaryById(request.delegateUserId())
                .orElseThrow(() -> new InvalidApprovalDelegationParticipantException(
                        "Supervisor substituto não encontrado."
                ));
        participantValidator.validateActiveSupervisor(delegate, "supervisor substituto");
        validatePeriod(request.startAt(), request.endAt(), now);

        boolean overlaps = approvalDelegationRepository.existsOverlappingDelegation(
                delegator.id(),
                EnumSet.of(ApprovalDelegationStatus.PENDING, ApprovalDelegationStatus.ACTIVE),
                request.startAt(),
                request.endAt()
        );
        if (overlaps) {
            throw new ApprovalDelegationOverlapException();
        }

        List<CrBranch> branches = crBranchRepository.findAllByResponsibleUserId(delegator.id());
        if (branches.isEmpty()) {
            throw new SupervisorWithoutCrBranchException();
        }

        ApprovalDelegation delegation = new ApprovalDelegation(
                delegator.id(),
                delegate.id(),
                request.startAt(),
                request.endAt(),
                now
        );
        branches.forEach(delegation::addBranch);
        ApprovalDelegation saved = approvalDelegationRepository.saveAndFlush(delegation);

        if (!request.startAt().isAfter(now)) {
            saved = activateApprovalDelegation.activate(saved.getId());
        }
        return mapper.toResponse(saved);
    }

    private void validatePeriod(LocalDateTime startAt, LocalDateTime endAt, LocalDateTime now) {
        if (startAt == null || endAt == null) {
            throw new InvalidApprovalDelegationPeriodException(
                    "As datas de início e término devem ser informadas."
            );
        }
        if (!startAt.isBefore(endAt)) {
            throw new InvalidApprovalDelegationPeriodException(
                    "A data de início deve ser anterior à data de término."
            );
        }
        if (!endAt.isAfter(now)) {
            throw new InvalidApprovalDelegationPeriodException(
                    "A data de término deve estar no futuro."
            );
        }
    }
}
