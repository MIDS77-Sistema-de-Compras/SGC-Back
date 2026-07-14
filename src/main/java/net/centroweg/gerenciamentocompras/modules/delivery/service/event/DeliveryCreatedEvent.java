package net.centroweg.gerenciamentocompras.modules.delivery.service.event;

import java.time.LocalDateTime;

public record DeliveryCreatedEvent(
        Long deliveryId,
        Long requestId,
        LocalDateTime createdAt
) {
}
