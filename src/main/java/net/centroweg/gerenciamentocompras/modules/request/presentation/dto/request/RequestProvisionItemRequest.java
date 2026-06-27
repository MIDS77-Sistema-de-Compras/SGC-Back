package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RequestProvisionItemRequest(
        @NotNull(message = "O ID do servico nao pode ser nulo.")
        @Positive(message = "O ID do servico deve ser maior que 0.")
        Long provisionId,

        String additionalInformation
) {
}