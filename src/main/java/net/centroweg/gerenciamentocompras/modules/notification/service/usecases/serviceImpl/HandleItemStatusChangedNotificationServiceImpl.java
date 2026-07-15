package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.DeliveryPublicApi;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationData;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusEmailContent;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusEmailContentFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusInternalNotificationContent;
import net.centroweg.gerenciamentocompras.modules.notification.service.factory.ItemStatusInternalNotificationFactory;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.RequestNotificationRecipientDeduplicator;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.HandleItemStatusChangedNotificationUseCase;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.ItemStatusChangedEmailSender;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandleItemStatusChangedNotificationServiceImpl implements HandleItemStatusChangedNotificationUseCase {

    private final RequestPublicApi requestPublicApi;
    private final DeliveryPublicApi deliveryPublicApi;
    private final CreateInternalNotificationUseCase createInternalNotificationUseCase;
    private final ItemStatusInternalNotificationFactory internalNotificationFactory;
    private final ItemStatusEmailContentFactory emailContentFactory;
    private final RequestNotificationRecipientDeduplicator recipientDeduplicator;
    private final ItemStatusChangedEmailSender emailSender;

    @Override
    public void handle(ItemStatusChangedEvent event) {
        RequestNotificationData request = requestPublicApi.findNotificationDataById(event.requestId());
        Optional<DeliveryNotificationData> delivery = findRelatedDelivery(event);
        ItemStatusInternalNotificationContent internalContent = internalNotificationFactory.build(event, delivery);
        ItemStatusEmailContent emailContent = emailContentFactory.build(event, delivery);
        List<Long> userIds = recipientDeduplicator.distinctUserIds(request.recipients());

        try {
            createInternalNotificationUseCase.createNotifications(
                    internalContent.title(),
                    internalContent.message(),
                    event.requestId(),
                    userIds
            );
        } catch (RuntimeException exception) {
            log.error(
                    "Falha ao criar notificacao interna de status de item. requestId={}, itemId={}, userIds={}",
                    event.requestId(),
                    event.itemId(),
                    userIds,
                    exception
            );
            throw exception;
        }

        emailSender.sendEmails(event, request.recipients(), emailContent);
    }

    private Optional<DeliveryNotificationData> findRelatedDelivery(ItemStatusChangedEvent event) {
        if (!emailContentFactory.isDelivered(event.newStatusName())) {
            return Optional.empty();
        }
        return event.itemType() == RequestItemType.PRODUCT
                ? deliveryPublicApi.findActiveDeliveryByProductItem(event.requestId(), event.itemId())
                : deliveryPublicApi.findActiveDeliveryByProvisionItem(event.requestId(), event.itemId());
    }
}
