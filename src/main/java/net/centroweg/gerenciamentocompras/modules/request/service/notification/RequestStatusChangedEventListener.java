package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RequestStatusChangedEventListener {

    private final CreateRequestStatusNotificationServiceImpl createNotificationService;
    private final SendRequestStatusChangedEmailServiceImpl sendEmailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRequestStatusChanged(RequestStatusChangedEvent event) {
        createNotificationService.createNotifications(event);
        sendEmailService.sendEmails(event);
    }
}
