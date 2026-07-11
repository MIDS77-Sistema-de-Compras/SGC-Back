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
        List<DeliveryReceiverResponse> receivers,
        List<Long> productItemIds,
        List<Long> provisionItemIds
) {
    public DeliveryResponse(
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
        this(
                id,
                requestId,
                statusId,
                statusName,
                expectedDeliveryAt,
                deliveredAt,
                deliveryLocation,
                description,
                proofUrl,
                active,
                createdAt,
                updatedAt,
                receivers,
                List.of(),
                List.of()
        );
    }
}
