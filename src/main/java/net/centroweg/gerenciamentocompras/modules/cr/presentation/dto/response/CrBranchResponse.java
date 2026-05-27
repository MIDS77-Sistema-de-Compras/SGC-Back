package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

public record CrBranchResponse(
        Long id,
        String branchName,
        String crName,
        String crCode,
        String responsibleUserName
) {
}
