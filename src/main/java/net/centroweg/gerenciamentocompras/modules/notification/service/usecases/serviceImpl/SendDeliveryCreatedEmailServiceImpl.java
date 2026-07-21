package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryCreatedNotificationData;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationRecipient;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.DeliveryCreatedNotificationContent;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.DeliveryNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.EmailNotificationPreferenceFilter;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.DeliveryCreatedEmailSender;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendDeliveryCreatedEmailServiceImpl implements DeliveryCreatedEmailSender {

    private final EmailSenderService emailSenderService;
    private final DeliveryNotificationRecipientDeduplicator recipientDeduplicator;
    private final EmailNotificationPreferenceFilter preferenceFilter;

    @Async
    @Override
    public void sendEmails(DeliveryCreatedEvent event, DeliveryCreatedNotificationData delivery, DeliveryCreatedNotificationContent content) {
        preferenceFilter.filterEnabled(
                recipientDeduplicator.distinctEmails(delivery.recipients()),
                DeliveryNotificationRecipient::userId
        ).forEach(recipient -> {
            try {
                emailSenderService.sendEmail(new DefaultEmail(content.emailSubject(), recipient.email()), content.emailHtml());
            } catch (Exception exception) {
                log.error("Falha ao enviar e-mail de entrega. deliveryId={}, requestId={}, userId={}, email={}",
                        event.deliveryId(), event.requestId(), recipient.userId(), recipient.email(), exception);
            }
        });
    }
}