package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import java.util.List;

/**
 * Dados de saída que representam um vínculo entre CR e filial na resposta da API.
 *
 * @param id identificador do vínculo
 * @param branchName nome da filial associada
 * @param crName nome do Centro de Responsabilidade
 * @param crCode código do Centro de Responsabilidade
 * @param responsibleUsersName nome do usuário responsável (nulo quando não há responsável)
 */
public record CrBranchResponse(
        Long id,
        String branchName,
        String crName,
        String crCode,
        List<String> responsibleUsersName
) {
}