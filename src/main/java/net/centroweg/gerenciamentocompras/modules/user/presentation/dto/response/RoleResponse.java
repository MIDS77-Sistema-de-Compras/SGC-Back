package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * DTO de saída com os dados de um {@link Role}.
 * @param id identificador do nível de acesso.
 * @param name nome do nível de acesso.
 */
public record RoleResponse(
        Long id,

        String name
) {
}
