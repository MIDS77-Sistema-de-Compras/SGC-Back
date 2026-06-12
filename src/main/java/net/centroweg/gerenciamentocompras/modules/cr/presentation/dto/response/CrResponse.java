package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

public record CrResponse (
        Long id,
        String name,
        String code,
        Boolean master
){
}
