package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RequestRequest(

        @NotNull(message = "A filial/CR é obrigatória.")
        Long crBranchId,

        @NotNull(message = "O nome do status não pode ser vazio")
        String statusName,

        @NotNull(message = "a lista de usuários atribuidos não pode ser vazio")
        @Size(max = 2, message = "Apenas 3 usuários incluindo você pode ser atribuido a uma solicitação")
        List<Long> userIds
) {}
