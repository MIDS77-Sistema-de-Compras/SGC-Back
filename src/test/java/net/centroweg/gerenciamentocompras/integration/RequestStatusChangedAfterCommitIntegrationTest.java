package net.centroweg.gerenciamentocompras.integration;

import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.HandleRequestStatusChangedNotificationUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest
@ActiveProfiles("test")
class RequestStatusChangedAfterCommitIntegrationTest {

    @Autowired private ApplicationEventPublisher eventPublisher;
    @Autowired private PlatformTransactionManager transactionManager;

    @MockitoBean private HandleRequestStatusChangedNotificationUseCase notificationUseCase;

    @Test
    void shouldNotProcessEventWhenTransactionRollsBack() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        RequestStatusChangedEvent event = new RequestStatusChangedEvent(
                10L,
                1L,
                "Aprovado",
                2L,
                "Em atendimento",
                null,
                20L,
                "Responsável",
                LocalDateTime.now()
        );

        assertThrows(IllegalStateException.class, () -> transactionTemplate.executeWithoutResult(status -> {
            eventPublisher.publishEvent(event);
            throw new IllegalStateException("Forçar rollback");
        }));

        verifyNoInteractions(notificationUseCase);
    }
}
