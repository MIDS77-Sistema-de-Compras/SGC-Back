package net.centroweg.gerenciamentocompras.integration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.HandleItemStatusChangedNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;

@SpringBootTest
@ActiveProfiles("test")
class ItemStatusChangedAfterCommitIntegrationTest {

    @Autowired private ApplicationEventPublisher eventPublisher;
    @Autowired private PlatformTransactionManager transactionManager;
    @Autowired private static RequestRepository repository;
    @MockitoBean private HandleItemStatusChangedNotificationUseCase notificationUseCase;

    @Test
    void shouldProcessEventOnlyAfterCommit() {
        ItemStatusChangedEvent event = event();
        new TransactionTemplate(transactionManager).executeWithoutResult(status -> eventPublisher.publishEvent(event));
        verify(notificationUseCase).handle(event);
    }

    @Test
    void shouldNotProcessEventWhenMainTransactionRollsBack() {
        ItemStatusChangedEvent event = event();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        assertThrows(IllegalStateException.class, () -> transactionTemplate.executeWithoutResult(status -> {
            eventPublisher.publishEvent(event);
            throw new IllegalStateException("Forcar rollback");
        }));

        verifyNoInteractions(notificationUseCase);
    }

    private ItemStatusChangedEvent event() {
        return new ItemStatusChangedEvent(
                1L, 1L, RequestItemType.PRODUCT, "Parafuso", "P-1", 2.0, "UN",
                "Aprovado", "Entregue", null, LocalDateTime.now()
        );
    }
}
