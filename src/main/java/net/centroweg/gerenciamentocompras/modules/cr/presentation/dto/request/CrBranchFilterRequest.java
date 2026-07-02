package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;

/**
 * DTO de entrada para criação de filtros possíveis com dados da {@link CrBranch}.
 * @param crCode código do CR, não pode ser nulo e nem vazio.
 * @param crName nome do CR, não pode ser nulo e nem vazio.
 * @param responsibleName lista que contém o nome de todos os responsáveis pela requisição.
 */
public record CrBranchFilterRequest (
        @NotBlank(message = "O código do CR não deve ser nulo e nem vazio!")
        String crCode,
        @NotBlank(message = "O nome do CR não deve ser nulo e nem vazio!")
        String crName,
        List<String> responsibleName
){
}
