package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.RequestStatusEmailContentFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.EmailNotificationPreferenceFilter;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.RequestNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.RequestStatusChangedEmailSender;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestStatusNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendRequestStatusChangedEmailServiceImpl implements RequestStatusChangedEmailSender {

    private final EmailSenderService emailSenderService;
    private final RequestNotificationRecipientDeduplicator recipientDeduplicator;
    private final RequestStatusEmailContentFactory emailFactory;
    private final EmailNotificationPreferenceFilter preferenceFilter;

    @Async
    @Override
    public void sendEmails(RequestStatusChangedEvent event, RequestStatusNotificationData request) {
        recipientDeduplicator.distinctRecipientsByUserId(request.recipients()).stream()
                .filter(recipient -> recipient.email() == null || recipient.email().isBlank())
                .forEach(recipient -> log.info("Solicitante sem e-mail. requestId={}, userId={}, novoStatus={}",
                        event.requestId(), recipient.userId(), event.newStatusName()));
        preferenceFilter.filterEnabled(
                recipientDeduplicator.distinctEmailRecipients(request.recipients()),
                net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationRecipient::userId
        ).forEach(recipient -> {
            try {
                var content = emailFactory.build(event, request, recipient.userName());
                emailSenderService.sendEmail(new DefaultEmail(content.subject(), recipient.email()), content.html());
            } catch (Exception exception) {
                log.error("Falha ao enviar e-mail de status. requestId={}, userId={}, email={}, novoStatus={}",
                        event.requestId(), recipient.userId(), recipient.email(), event.newStatusName(), exception);
            }
        });
    }
}