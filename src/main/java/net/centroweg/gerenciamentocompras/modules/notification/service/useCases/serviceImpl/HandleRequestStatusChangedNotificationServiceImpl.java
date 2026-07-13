package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.RequestStatusInternalNotificationFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.RequestNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.HandleRequestStatusChangedNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.RequestStatusChangedEmailSender;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HandleRequestStatusChangedNotificationServiceImpl implements HandleRequestStatusChangedNotificationUseCase {

    private final RequestPublicApi requestPublicApi;
    private final CreateInternalNotificationUseCase createInternalNotificationUseCase;
    private final RequestStatusInternalNotificationFactory internalFactory;
    private final RequestNotificationRecipientDeduplicator recipientDeduplicator;
    private final RequestStatusChangedEmailSender emailSender;

    @Override
    public void handle(RequestStatusChangedEvent event) {
        var request = requestPublicApi.findStatusNotificationDataById(event.requestId());
        var internalContent = internalFactory.build(event);
        createInternalNotificationUseCase.createNotifications(
                internalContent.title(), internalContent.message(), event.requestId(),
                recipientDeduplicator.distinctUserIds(request.recipients())
        );
        emailSender.sendEmails(event, request);
    }
}
