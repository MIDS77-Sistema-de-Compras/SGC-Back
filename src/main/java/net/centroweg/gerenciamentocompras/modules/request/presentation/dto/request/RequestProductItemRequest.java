package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RequestProductItemRequest(
        @NotBlank(message = "O nome do produto é obrigatorio.")
        String productName,

        @Size(max = 100, message = "A variação excede o limite máximo permitido (100 caracteres).")
        String variation,

        @NotBlank(message = "A unidade de medida é obrigatoria.")
        String measurementUnit,

        @NotNull(message = "A quantidade é obrigatoria.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        Double quantity,

        @NotBlank(message = "As informações adicionais do produto não podem estar em branco.")
        @Size(max = 255, message = "Informações adicionais excedem o limite máximo permitido (255 caractéres)")
        String additionalInformations
) {
}
