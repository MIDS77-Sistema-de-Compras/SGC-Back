package net.centroweg.gerenciamentocompras.modules.delivery.service.api;

import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationData;
import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryCreatedNotificationData;

import java.util.Optional;

public interface DeliveryPublicApi {

    Optional<DeliveryNotificationData> findActiveDeliveryByProductItem(Long requestId, Long itemId);

    Optional<DeliveryNotificationData> findActiveDeliveryByProvisionItem(Long requestId, Long itemId);
    DeliveryCreatedNotificationData findNotificationDataById(Long deliveryId);
}
