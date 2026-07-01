package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.Positive;

public record RequestProvisionItemRequest(
        @Positive(message = "O ID do servico deve ser maior que 0.")
        Long provisionId,

        String name,
        Double totalValue,

        String description,

        String additionalInformation
) {
}
