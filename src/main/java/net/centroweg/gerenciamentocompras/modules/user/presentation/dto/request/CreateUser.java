package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

/**
 * DTO de entrada para criação e atualização de usuários.
 */

public record CreateUser(
        /**
         * Nome completo do usuário
         */
        @NotBlank(message = "Nome não deve estar em branco.")
        @Size(  min = 3,
                max = 100,
                message = "O nome deve ter entre 3 e 100 caracteres.")
        String name,

        /**
         * Endereço de e-mail do usuário
         */
        @NotBlank(message = "Email não deve estar em branco.")
        @Email(message = "Email inválido.")
        @Size(  min = 10,
                max = 120,
                message = "O email deve ter entre 10 e 120 caracteres.")
        String email,

        /**
         * CPF do usuário
         */
        @NotBlank(message = "CPF não deve estar em branco.")
        @CPF(message = "Deve ser um CPF válido.")
        @Pattern(regexp = "\\d{11}",
                 message = "O CPF deve conter apenas números.")
        String cpf,

        /**
         * Senha de acesso do usuário
         */
        @NotBlank(message = "A senha não deve estar em branco.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                 message = "A senha deve conter letra maiúscula e minúscula, número e caracteres especiais.")
        @Size(  min = 8,
                max = 30,
                message = "A senha deve ter entre 8 e 30 caracteres.")
        String password,

        /**
         * Ramal para contato interno com o usuário
         */
        @NotBlank(message = "O ramal não deve estar em branco.")
        @Size(  min = 4,
                max = 6,
                message = "O ramal deve ter entre 4 e 6 caracteres.")
        String extensionNumber,

        /**
         * Atividade do usuário
         */
        @NotNull(message = "A atividade do usuário deve ser informada.")
        Boolean active,

        /**
         * Nível de permissão do usuário
         */
        @NotBlank(message = "A permissão do usuário não deve estar em branco")
        String nameRole
) {
}
