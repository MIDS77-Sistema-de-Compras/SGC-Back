package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

public record CrCompoundResponse(
        long id,
        String name,
        String code,
        boolean master,
        String sector
){
}
