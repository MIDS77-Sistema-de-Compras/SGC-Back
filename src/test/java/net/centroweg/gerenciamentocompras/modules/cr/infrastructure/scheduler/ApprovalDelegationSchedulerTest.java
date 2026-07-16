package net.centroweg.gerenciamentocompras.modules.cr.infrastructure.scheduler;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.ApprovalDelegationStatus;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.ApprovalDelegationRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.ActivateApprovalDelegation;
import net.centroweg.gerenciamentocompras.modules.cr.service.approvaldelegationservice.functionality.FinishApprovalDelegation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalDelegationSchedulerTest {

    private static final Instant NOW = Instant.parse("2026-08-15T18:00:00Z");

    @Mock private ApprovalDelegationRepository repository;
    @Mock private ActivateApprovalDelegation activateUseCase;
    @Mock private FinishApprovalDelegation finishUseCase;

    private ApprovalDelegationScheduler scheduler;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
        now = LocalDateTime.ofInstant(NOW, ZoneOffset.UTC);
        scheduler = new ApprovalDelegationScheduler(repository, activateUseCase, finishUseCase, clock);
    }

    @Test
    void shouldProcessDueActivationAndFinishIds() {
        when(repository.findIdsReadyForActivation(ApprovalDelegationStatus.PENDING, now))
                .thenReturn(List.of(1L, 2L));
        when(repository.findIdsReadyForFinish(ApprovalDelegationStatus.ACTIVE, now))
                .thenReturn(List.of(3L));

        scheduler.processDueDelegations();

        verify(activateUseCase).activate(1L);
        verify(activateUseCase).activate(2L);
        verify(finishUseCase).finish(3L);
    }

    @Test
    void shouldContinueAfterOneDelegationFails() {
        when(repository.findIdsReadyForActivation(ApprovalDelegationStatus.PENDING, now))
                .thenReturn(List.of(1L, 2L));
        when(repository.findIdsReadyForFinish(ApprovalDelegationStatus.ACTIVE, now))
                .thenReturn(List.of(3L, 4L));
        doThrow(new RuntimeException("falha simulada")).when(activateUseCase).activate(1L);
        doThrow(new RuntimeException("falha simulada")).when(finishUseCase).finish(3L);

        scheduler.processDueDelegations();

        InOrder activationOrder = inOrder(activateUseCase);
        activationOrder.verify(activateUseCase).activate(1L);
        activationOrder.verify(activateUseCase).activate(2L);
        InOrder finishOrder = inOrder(finishUseCase);
        finishOrder.verify(finishUseCase).finish(3L);
        finishOrder.verify(finishUseCase).finish(4L);
    }

    @Test
    void shouldNotInvokeUseCasesWhenNoDelegationIsDue() {
        when(repository.findIdsReadyForActivation(ApprovalDelegationStatus.PENDING, now))
                .thenReturn(List.of());
        when(repository.findIdsReadyForFinish(ApprovalDelegationStatus.ACTIVE, now))
                .thenReturn(List.of());

        scheduler.processDueDelegations();

        verifyNoInteractions(activateUseCase, finishUseCase);
    }
}
