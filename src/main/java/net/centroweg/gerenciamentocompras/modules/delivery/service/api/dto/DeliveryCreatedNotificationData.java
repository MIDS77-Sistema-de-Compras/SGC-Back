package net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DeliveryCreatedNotificationData(
        Long deliveryId,
        Long requestId,
        Long statusId,
        String statusName,
        LocalDateTime expectedDeliveryAt,
        LocalDateTime deliveredAt,
        String deliveryLocation,
        String description,
        List<DeliveryNotificationRecipient> recipients,
        List<DeliveryProductNotificationData> productItems,
        List<String> provisionItemNames
) {
    public DeliveryCreatedNotificationData {
        recipients = recipients == null ? List.of() : List.copyOf(recipients);
        productItems = productItems == null ? List.of() : List.copyOf(productItems);
        provisionItemNames = provisionItemNames == null ? List.of() : List.copyOf(provisionItemNames);
    }
}
