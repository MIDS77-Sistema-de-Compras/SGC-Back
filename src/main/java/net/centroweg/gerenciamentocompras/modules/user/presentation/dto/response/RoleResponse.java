package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response;

/** DTO contendo dados da role que serão retornados ao usuário */

public record RoleResponse(
        Long id,
        String name
) {
}
