package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import java.util.List;

public record CrBranchFilterRequest (
        String crCode,
        String crName,
        List<String> responsibleName
){
}
