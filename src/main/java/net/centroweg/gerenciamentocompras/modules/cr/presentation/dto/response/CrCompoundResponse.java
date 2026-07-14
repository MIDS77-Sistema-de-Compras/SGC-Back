package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;

/**
 * DTO de saída com os dados completos de um {@link Cr}.
 * @param id identificador do CR.
 * @param name nome do CR.
 * @param code código do CR.
 * @param master booleano que indica se é o CR master ou não.
 * @param sector nome do bloco que a CR pertence.
 */

public record CrCompoundResponse (
        long id,
        String name,
        String code,
        Boolean master,
        String sector
){
}
