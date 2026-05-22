package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response;

public record UserRole(
        UserResponse user,
        RoleResponse role
) {
}
