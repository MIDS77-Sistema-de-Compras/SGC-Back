package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.HandleItemStatusChangedNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ItemStatusChangedEventListener {

    private final HandleItemStatusChangedNotificationUseCase notificationUseCase;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onItemStatusChanged(ItemStatusChangedEvent event) {
        notificationUseCase.handle(event);
    }
}
