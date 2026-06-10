package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemRequestProductRequest(
        @NotNull
        @Positive
        long requestId,

        @NotBlank
        String productName,

        @NotBlank
        String measurementUnit,

        @NotNull
        double quantity,

        @NotBlank
        String statusName,

        @NotBlank
        String additionalInformations
) {
}
