package net.centroweg.gerenciamentocompras.modules.user.service.api.dto;

/**
 * Visão pública mínima de um usuário para comunicação entre módulos.
 */
public record UserSummaryPublicResponse(
        Long id,
        String name,
        boolean active,
        String role
) {
}
