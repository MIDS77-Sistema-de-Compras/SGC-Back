package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateRequestStatus(

        @NotBlank(message = "O status é obrigatório.")
        String statusName,

        String justification
) {
}