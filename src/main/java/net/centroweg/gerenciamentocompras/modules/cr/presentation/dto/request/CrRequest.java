package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrRequest(
        @NotBlank(message = "O nome do CR é obrigatório") String name,
        @NotBlank(message = "O código do CR é obrigatório") String code,
        boolean master,
        @NotBlank(message = "O nome do bloco não pode estar vazio") String sector
) {
}
