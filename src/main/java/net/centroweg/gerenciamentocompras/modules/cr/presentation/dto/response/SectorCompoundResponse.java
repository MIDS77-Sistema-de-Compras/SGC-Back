package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import java.util.List;

public record SectorCompoundResponse(
        Long id,
        String name,
        List<CrSimpleResponse> crs
) {
}
