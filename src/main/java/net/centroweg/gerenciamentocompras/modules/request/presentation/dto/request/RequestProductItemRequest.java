package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RequestProductItemRequest(
        @NotBlank(message = "O nome do produto e obrigatorio.")
        String productName,

        @NotBlank(message = "A unidade de medida e obrigatoria.")
        String measurementUnit,

        @NotNull(message = "A quantidade e obrigatoria.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        Double quantity,

        String additionalInformations
) {
}