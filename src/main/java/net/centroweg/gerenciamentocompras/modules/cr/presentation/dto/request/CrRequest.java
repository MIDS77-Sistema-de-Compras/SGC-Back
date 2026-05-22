package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

public record CrRequest(

        String name,
        long code,
        boolean master
) {
}
