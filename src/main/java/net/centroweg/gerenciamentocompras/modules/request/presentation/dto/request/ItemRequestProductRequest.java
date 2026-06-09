package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemRequestProductRequest(
        @NotNull
        long requestId,

        @NotNull
        String productName,

        @NotNull
        String measurementUnit,

        @NotNull
        double quantity,
        String statusName,

        @NotBlank
        String additionalInformations
) {
}
