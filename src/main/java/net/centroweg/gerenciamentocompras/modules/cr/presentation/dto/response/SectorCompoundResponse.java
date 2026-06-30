package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * DTO de saída com dados completos do {@link Sector}.
 * @param id identificador único do setor.
 * @param name nome do setor.
 * @param crs lista de CRs que pertencem a um setor.
 */
public record SectorCompoundResponse(
        Long id,
        String name,
        List<CrSimpleResponse> crs
) {
}
