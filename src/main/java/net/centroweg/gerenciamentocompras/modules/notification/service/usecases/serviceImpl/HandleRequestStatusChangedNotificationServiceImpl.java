package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.RequestStatusInternalNotificationFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.RequestNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.HandleRequestStatusChangedNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.RequestStatusChangedEmailSender;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;

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
                internalContent.title(), internalContent.message(), NotificationType.STATUS_ALTERADO, event.requestId(),
                recipientDeduplicator.distinctUserIds(request.recipients())
        );
        emailSender.sendEmails(event, request);
    }
}
