package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

public record CrBranchFilterRequest (
        String crCode,
        String crName,
        String responsibleName
){
}
