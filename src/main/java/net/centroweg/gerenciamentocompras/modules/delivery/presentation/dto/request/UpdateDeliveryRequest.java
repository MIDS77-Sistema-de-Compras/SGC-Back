package net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateDeliveryRequest(
        @NotNull(message = "O status e obrigatorio.")
        Long statusId,

        @NotNull(message = "A data prevista de entrega e obrigatoria.")
        LocalDateTime expectedDeliveryAt,

        LocalDateTime deliveredAt,

        @NotBlank(message = "O local de entrega e obrigatorio.")
        String deliveryLocation,

        String description,

        String proofUrl,

        @NotNull(message = "Os recebedores sao obrigatorios.")
        @Size(min = 2, max = 2, message = "A entrega deve possuir exatamente dois recebedores.")
        List<@NotNull(message = "O id do recebedor e obrigatorio.") Long> receiverIds,

        List<Long> productItemIds,

        List<Long> provisionItemIds
) {
    public UpdateDeliveryRequest(
            Long statusId,
            LocalDateTime expectedDeliveryAt,
            LocalDateTime deliveredAt,
            String deliveryLocation,
            String description,
            String proofUrl,
            List<Long> receiverIds
    ) {
        this(statusId, expectedDeliveryAt, deliveredAt, deliveryLocation, description, proofUrl, receiverIds, null, null);
    }
}
