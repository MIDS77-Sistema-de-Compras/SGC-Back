package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RequestProvisionItemRequest(
        @Positive(message = "O ID do servico deve ser maior que 0.")
        Long provisionId,

        @NotBlank(message = "O nome do item não pode estar em branco.")
        String name,

        @NotNull(message = "O valor total não pode ser nulo.")
        @Positive(message = "O valor total deve ser maior que zero.")
        Double totalValue,

        @NotBlank(message = "A descrição do item não pode estar em branco.")
        @Size(min=10, max=100, message = "A descrição do item deve conter entre 10 e 100 caractéres.")
        String description,

        @NotBlank(message = "As informações adicionais não podem estar em branco.")
        @Max(value=255, message = "Informações adicionais excedem o limite máximo permitido (255 caractéres)")
        String additionalInformation
) {
}
