package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

/**
 * DTO de saída com os dados de uma {@link net.centroweg.gerenciamentocompras.modules.cr.domain.Branch}.
 *
 * @param id   identificador único da branch
 * @param name nome da branch
 * @author Leandro
 */
public record BranchResponse (
        Long id,
        String name
){
}
