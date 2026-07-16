package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * DTO de saída com dados simples de um {@link Sector}.
 * @param id identificador do bloco.
 * @param name nome do bloco.
 */
public record SectorSimpleResponse(
        Long id,
        String name
) {
}
