package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SectorRequest(
        @NotBlank
        String name
) {
}
