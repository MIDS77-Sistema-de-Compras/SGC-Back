package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.ApprovalDelegationStatus;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.ApprovalDelegationRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.ActivateApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.FinishApprovalDelegation;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalDelegationScheduler {

    private final ApprovalDelegationRepository approvalDelegationRepository;
    private final ActivateApprovalDelegation activateApprovalDelegation;
    private final FinishApprovalDelegation finishApprovalDelegation;
    private final Clock clock;

    @Scheduled(fixedDelayString = "${app.approval-delegation.scheduler-delay-ms:60000}")
    public void processDueDelegations() {
        LocalDateTime now = LocalDateTime.now(clock);
        List<Long> readyForActivation = approvalDelegationRepository.findIdsReadyForActivation(
                ApprovalDelegationStatus.PENDING, now
        );
        processIndividually(readyForActivation, activateApprovalDelegation::activate, "ativar");

        List<Long> readyForFinish = approvalDelegationRepository.findIdsReadyForFinish(
                ApprovalDelegationStatus.ACTIVE, now
        );
        processIndividually(readyForFinish, finishApprovalDelegation::finish, "finalizar");
    }

    private void processIndividually(List<Long> delegationIds, Consumer<Long> action, String operation) {
        for (Long delegationId : delegationIds) {
            try {
                action.accept(delegationId);
            } catch (RuntimeException exception) {
                log.error(
                        "Falha ao {} a delegação de aprovação de ID {}: {}",
                        operation,
                        delegationId,
                        exception.getClass().getSimpleName()
                );
            }
        }
    }
}
