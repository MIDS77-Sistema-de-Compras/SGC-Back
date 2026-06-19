package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemRequestProductRequest(
        @NotNull
        @Positive
        Long requestId,

        @NotBlank
        String productName,

        @NotBlank
        String measurementUnit,

        @NotNull
        Double quantity,

        @NotBlank
        String statusName,

        String additionalInformations
) {
}
