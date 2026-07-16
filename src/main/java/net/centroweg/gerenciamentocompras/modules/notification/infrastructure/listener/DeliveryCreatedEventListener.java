package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.HandleDeliveryCreatedNotificationUseCase;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeliveryCreatedEventListener {

    private final HandleDeliveryCreatedNotificationUseCase useCase;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDeliveryCreated(DeliveryCreatedEvent event) {
        useCase.handle(event);
    }
}
