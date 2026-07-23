package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * DTO de entrada para autenticação de um {@link User}.
 * @param userName endereço de email ou CPF do usuário, não pode ser nulo ou vazio.
 * @param password senha de acesso do usuário, não pode ser nula ou vazia.
 */
public record LogIn(
        @NotBlank(message = "O nome de usuário não deve ser nulo ou vazio!")
        String userName,

        @NotBlank(message = "A senha não deve ser nula ou vazia!")
        String password
) {
}
