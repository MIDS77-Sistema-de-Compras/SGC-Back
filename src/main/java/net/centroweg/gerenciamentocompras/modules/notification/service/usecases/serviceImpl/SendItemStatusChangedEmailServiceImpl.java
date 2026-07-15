package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusEmailContent;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.RequestNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.ItemStatusChangedEmailSender;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationRecipient;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendItemStatusChangedEmailServiceImpl implements ItemStatusChangedEmailSender {

    private final EmailSenderService emailSenderService;
    private final RequestNotificationRecipientDeduplicator recipientDeduplicator;

    @Async
    @Override
    public void sendEmails(
            ItemStatusChangedEvent event,
            List<RequestNotificationRecipient> recipients,
            ItemStatusEmailContent content
    ) {
        recipientDeduplicator.distinctRecipientsByUserId(recipients).stream()
                .filter(recipient -> recipient.email() == null || recipient.email().isBlank())
                .forEach(recipient -> log.info(
                        "Solicitante sem e-mail; notificacao interna mantida. requestId={}, itemId={}, userId={}, novoStatus={}",
                        event.requestId(),
                        event.itemId(),
                        recipient.userId(),
                        event.newStatusName()
                ));

        recipientDeduplicator.distinctEmailRecipients(recipients)
                .forEach(recipient -> sendToRecipient(event, recipient, content));
    }

    private void sendToRecipient(
            ItemStatusChangedEvent event,
            RequestNotificationRecipient recipient,
            ItemStatusEmailContent content
    ) {
        try {
            emailSenderService.sendEmail(
                    new DefaultEmail(content.subject(), recipient.email()),
                    content.html()
            );
        } catch (Exception exception) {
            log.error(
                    "Falha ao enviar e-mail de status de item. requestId={}, itemId={}, userId={}, email={}, novoStatus={}",
                    event.requestId(),
                    event.itemId(),
                    recipient.userId(),
                    recipient.email(),
                    event.newStatusName(),
                    exception
            );
        }
    }
}
