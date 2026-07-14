package net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DeliveryNotificationData(
        Long deliveryId,
        LocalDateTime deliveredAt,
        String deliveryLocation,
        String description,
        List<String> receiverNames,
        List<DeliveryProductNotificationData> productItems,
        List<String> provisionItemNames
) {
    public DeliveryNotificationData {
        receiverNames = receiverNames == null ? List.of() : List.copyOf(receiverNames);
        productItems = productItems == null ? List.of() : List.copyOf(productItems);
        provisionItemNames = provisionItemNames == null ? List.of() : List.copyOf(provisionItemNames);
    }
}
