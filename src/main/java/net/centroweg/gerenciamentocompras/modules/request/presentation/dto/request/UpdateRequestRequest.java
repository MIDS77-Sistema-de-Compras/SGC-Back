package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateRequestRequest(

        @NotNull(message = "A filial/CR é obrigatória.")
        Long crBranchId,

        @NotBlank(message = "O status é obrigatório.")
        String statusName
) {}
