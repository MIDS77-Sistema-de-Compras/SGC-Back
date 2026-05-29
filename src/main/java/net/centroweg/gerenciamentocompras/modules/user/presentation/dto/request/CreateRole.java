package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** DTO contendo dados necessários para criar uma role */

public record CreateRole(
        @NotBlank
        @Size(  min = 3,
                max = 100,
                message = "O nome deve ter entre 3 e 100 caracteres.")
        String name
) {
}
