package net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request;

import jakarta.validation.constraints.Size;

public record ConfirmDeliveryReceiverRequest(
        @Size(max = 500, message = "A observacao deve ter no maximo 500 caracteres.")
        String observation
) {
}
