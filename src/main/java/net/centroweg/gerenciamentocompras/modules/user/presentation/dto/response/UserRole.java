package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response;

import java.util.List;

public record UserRole(
        List<UserResponse> users,
        RoleResponse role
) {
}
