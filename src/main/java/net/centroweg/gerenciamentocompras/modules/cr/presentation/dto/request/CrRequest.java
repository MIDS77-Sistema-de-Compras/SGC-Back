package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrRequest(

        @NotBlank(message = "O nome do CR é obrigatório")
        String name,

        @NotNull(message = "O código do CR é obrigatório")
        @Positive(message = "O código do CR deve ser positivo")
        long code,

        boolean master
) {
}
