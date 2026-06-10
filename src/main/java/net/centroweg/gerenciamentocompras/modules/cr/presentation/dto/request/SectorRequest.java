package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SectorRequest(
        @NotBlank(message = "O nome não deve estar em branco")
        String name
) {
}
