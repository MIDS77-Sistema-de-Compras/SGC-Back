package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RequestRequest(

        @NotNull(message = "A filial/CR é obrigatória.")
        Long crBranchId,

        String statusName,

        @Size(max = 2, message = "Apenas 3 usuários incluindo você pode ser atribuido a uma solicitação")
        List<Long> userIds
) {}
