package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import java.util.List;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * DTO de saída com dados completos do {@link Sector}.
 * @param id identificadordo bloco.
 * @param name nome do bloco.
 * @param crs lista de CRs que pertencem a um bloco.
 */
public record SectorCompoundResponse(
        Long id,
        String name,
        List<CrSimpleResponse> crs
) {
}
