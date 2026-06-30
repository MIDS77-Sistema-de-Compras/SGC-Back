package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;

/**
 * DTO de saída com dados que representam um vínculo entre CR e filial na {@link CrBranch}.
 * @param id identificador único do vínculo.
 * @param branchName nome da branch associada.
 * @param crName nome do CR associado.
 * @param crCode código do CR associado.
 * @param responsibleUsersName lista com nomes dos usuários responsáveis pela requisição.
 */
public record CrBranchResponse(
        Long id,
        String branchName,
        String crName,
        String crCode,
        List<String> responsibleUsersName
) {
}