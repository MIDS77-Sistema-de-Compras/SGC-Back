package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogIn(
        @NotBlank(message = "O Email ou CPF é obrigatório")
        String userName,
        @NotBlank(message = "A senha é obrigatória")
        String password
) {
}
