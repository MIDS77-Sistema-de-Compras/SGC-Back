package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RequestProductItemRequest(
        @NotBlank(message = "O nome do produto é obrigatorio.")
        String productName,

        @NotBlank(message = "A unidade de medida é obrigatoria.")
        String measurementUnit,

        @NotNull(message = "A quantidade é obrigatoria.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        Double quantity,

        @NotBlank(message = "As informações adicionais do produto não podem estar em branco.")
        @Max(value=255, message = "Informações adicionais excedem o limite máximo permitido (255 caractéres)")
        String additionalInformations
) {
}