package net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record DeliveryResponse(
        Long id,
        Long requestId,
        Long statusId,
        String statusName,
        LocalDateTime expectedDeliveryAt,
        LocalDateTime deliveredAt,
        String deliveryLocation,
        String description,
        String proofUrl,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<DeliveryReceiverResponse> receivers
) {
}
