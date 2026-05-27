package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

import java.util.List;

public record CreateRequestRequest(

        @NotNull(message = "A filial/CR é obrigatória.")
        Long crBranchId,

        @NotNull(message = "A solicitação deve conter um status.")
        Long status,

        @NotNull(message = "A solicitação deve conter ao menos um usuário.")
        List<User> usuarios
) {}
