package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ItemRequestProductRequest(
        @NotNull(message = "O ID da solicitação não pode ser nulo.")
        @Positive(message = "O ID da solicitação deve ser maior que 0.")
        Long requestId,

        @NotBlank(message = "O nome do produto não pode estar em branco.")
        String productName,

        @Size(max = 100, message = "Variação excede o limite máximo permitido (100 caractéres).")
        String type,

        @NotBlank(message = "A unidade de medida não pode estar em branco.")
        String measurementUnit,

        @NotNull(message = "Forneça a quantidade do produto.")
        Double quantity,

        @NotBlank(message = "O status do produto não pode estar em branco.")
        String statusName,

        @Size(max = 255, message = "Informações adicionais excedem o limite máximo permitido (255 caractéres).")
        String additionalInformations
) {}
