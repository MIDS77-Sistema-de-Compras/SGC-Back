package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestRequest(

        @NotNull(message = "A filial/CR é obrigatória.")
        Long crBranchId,

        @NotBlank(message = "A solicitação deve conter um status.")
        String statusName
) {}
