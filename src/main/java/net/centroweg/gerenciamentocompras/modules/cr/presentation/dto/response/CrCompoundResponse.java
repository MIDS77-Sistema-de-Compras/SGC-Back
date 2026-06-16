package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

public record CrCompoundResponse(
        Long id,
        String name,
        String code,
        Boolean master,
        String sector
){
}
