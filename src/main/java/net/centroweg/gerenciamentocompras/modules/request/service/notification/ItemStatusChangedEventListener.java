package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ItemStatusChangedEventListener {

    private final CreateItemStatusNotificationServiceImpl createNotificationService;
    private final SendItemStatusChangedEmailServiceImpl sendEmailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onItemStatusChanged(ItemStatusChangedEvent event) {
        createNotificationService.createNotifications(event);
        sendEmailService.sendEmails(event);
    }
}
