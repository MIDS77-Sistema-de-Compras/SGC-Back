package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

import java.util.List;

public record CreateRequestRequest(

        @NotNull(message = "A filial/CR é obrigatória.")
        Long crBranchId,

        @NotBlank(message = "A solicitação deve conter um status.")
        String statusName
) {}
