package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BranchRequest (
        @NotBlank String name
){
}
