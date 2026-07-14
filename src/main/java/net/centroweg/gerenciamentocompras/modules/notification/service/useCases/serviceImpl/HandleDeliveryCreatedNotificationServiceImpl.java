package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.DeliveryPublicApi;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.DeliveryCreatedNotificationContentFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.DeliveryNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.DeliveryCreatedEmailSender;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.HandleDeliveryCreatedNotificationUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HandleDeliveryCreatedNotificationServiceImpl implements HandleDeliveryCreatedNotificationUseCase {

    private final DeliveryPublicApi deliveryPublicApi;
    private final CreateInternalNotificationUseCase createInternalNotificationUseCase;
    private final DeliveryCreatedNotificationContentFactory contentFactory;
    private final DeliveryNotificationRecipientDeduplicator recipientDeduplicator;
    private final DeliveryCreatedEmailSender emailSender;

    @Override
    public void handle(DeliveryCreatedEvent event) {
        var delivery = deliveryPublicApi.findNotificationDataById(event.deliveryId());
        var content = contentFactory.build(delivery);
        createInternalNotificationUseCase.createNotifications(content.title(), content.internalMessage(), event.requestId(),
                recipientDeduplicator.distinctUserIds(delivery.recipients()));
        emailSender.sendEmails(event, delivery, content);
    }
}
