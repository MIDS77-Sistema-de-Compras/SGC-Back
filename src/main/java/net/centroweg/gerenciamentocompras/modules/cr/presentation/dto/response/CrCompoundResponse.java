package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

/**
 * DTO de saída com os dados de um Centro de Resultado (CR).
 *
 * @param id     identificador único do CR
 * @param name   nome do CR
 * @param code   código do CR
 * @param master indica se é o CR master
 */
public record CrCompoundResponse (
        long id,
        String name,
        String code,
        Boolean master,
        String sector
){
}
