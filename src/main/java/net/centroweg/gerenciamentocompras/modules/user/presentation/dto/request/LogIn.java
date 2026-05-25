package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogIn(
        @NotBlank
        String userName,
        @NotBlank
        String password
) {
}
