package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

/**
 * Dados de saída que representam um vínculo entre CR e filial na resposta da API.
 *
 * @param id identificador do vínculo
 * @param branchName nome da filial associada
 * @param crName nome do Centro de Responsabilidade
 * @param crCode código do Centro de Responsabilidade
 * @param responsibleUserName nome do usuário responsável (nulo quando não há responsável)
 */
public record CrBranchResponse(
        Long id,
        String branchName,
        String crName,
        String crCode,
        String responsibleUserName
) {
}