package net.centroweg.gerenciamentocompras.modules.notification.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.HandleRequestStatusChangedNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RequestStatusChangedEventListener {

    private final HandleRequestStatusChangedNotificationUseCase useCase;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRequestStatusChanged(RequestStatusChangedEvent event) {
        useCase.handle(event);
    }
}
