package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemRequestProductRequest(
        @NotNull(message = "O ID da solicitação não pode ser nulo.")
        @Positive(message = "O ID da solicitação deve ser maior que 0.")
        Long requestId,

        @NotBlank(message = "O nome do produto não pode estar em branco.")
        String productName,

        @NotBlank(message = "A unidade de medida não pode estar em branco.")
        String measurementUnit,

        @NotNull(message = "Forneça a quantidade do produto.")
        Double quantity,

        @NotBlank(message = "O status do produto não pode estar em branco.")
        String statusName,

        @NotBlank(message = "As informações adicionais do produto não podem estar em branco.")
        @Max(value=255, message = "Informações adicionais excedem o limite máximo permitido (255 caractéres).")
        String additionalInformations
) {}
