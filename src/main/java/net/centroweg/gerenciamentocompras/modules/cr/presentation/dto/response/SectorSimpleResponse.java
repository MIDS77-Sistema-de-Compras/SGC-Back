package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * DTO de saída com dados simples do {@link Sector}.
 * @param id identificador único do setor.
 * @param name nome do setor.
 */
public record SectorSimpleResponse(
        Long id,
        String name
) {
}
