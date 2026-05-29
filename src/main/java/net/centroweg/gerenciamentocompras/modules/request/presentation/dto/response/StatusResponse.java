package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;
/**
 * DTO responsável por representar os dados
 * retornados nas respostas da entidade Status.
 *
 * @param id identificador do status
 * @param name nome do status
 * @param description descrição do status
 *
 * @author André
 * @since 1.0
 */
public record StatusResponse(

        Long id,
        String name,
        String description

) {
}