package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response;

import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * DTO de saída com os dados de um {@link Role} e seus usuários.
 * @param users lista de usuários associados ao nível de acesso.
 * @param role dados do nível de acesso.
 */
public record UserRole(
        List<UserResponse> users,

        RoleResponse role
) {
}
