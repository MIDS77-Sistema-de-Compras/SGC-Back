package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;

/**
 * DTO de saída com os dados completos de um {@link Cr}.
 * @param id identificador único do CR.
 * @param name nome do CR.
 * @param code código do CR.
 * @param master boolean que indica se é o CR master(true) ou não(false).
 */
public record CrCompoundResponse (
        long id,
        String name,
        String code,
        Boolean master,
        String sector
){
}
