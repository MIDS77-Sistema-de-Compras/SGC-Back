package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;

/**
 * DTO de saída com os dados de uma {@link Branch}.
 * @param id identificador da filial.
 * @param name nome da filial.
 */
public record BranchResponse (
        Long id,

        String name
){
}
