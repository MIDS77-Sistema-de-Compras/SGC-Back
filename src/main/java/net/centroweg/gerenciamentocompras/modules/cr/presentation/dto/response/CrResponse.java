package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

public record CrResponse (
        long id,
        String name,
        String code,
        boolean master
){
}
