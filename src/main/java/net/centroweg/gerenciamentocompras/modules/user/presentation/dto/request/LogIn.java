package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para autenticação do usuário
 */

public record LogIn(
        /**
         * Nome de usuário que seria e-mail ou CPF
         */
        @NotBlank(message = "O Email ou CPF é obrigatório")
        String userName,

        /**
         * Senha de acesso do usuário
         */
        @NotBlank(message = "A senha é obrigatória")
        String password
) {
}
