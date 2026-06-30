package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;

/**
 * DTO de saída com dados simples de um {@link Cr}.
 * @param name nome do CR.
 */
public record CrSimpleResponse(
        String name
) {
}
