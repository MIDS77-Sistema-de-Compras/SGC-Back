package net.centroweg.gerenciamentocompras.modules.delivery.service.notification;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DeliveryCreatedEventListener {

    private final CreateDeliveryCreatedNotificationServiceImpl createNotificationService;
    private final SendDeliveryCreatedEmailServiceImpl sendEmailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDeliveryCreated(DeliveryCreatedEvent event) {
        createNotificationService.createNotifications(event);
        sendEmailService.sendEmails(event);
    }
}
