package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;

/**
 * DTO de saída com os dados de um {@link Status}.
 * @param id identificador do status.
 * @param name nome do status.
 * @param description descrição do status.
 */
public record StatusResponse(
        Long id,

        String name,

        String description

) {
}