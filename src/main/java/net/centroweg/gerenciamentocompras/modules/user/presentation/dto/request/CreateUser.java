package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * DTO de entrada para criação e atualização de um {@link User}.
 * @param name nome completo do usuário, não pode ser nulo ou vazio e deve ter entre 3 e 100 caracteres.
 * @param email endereço de email do usuário, não pode ser nulo ou vazio, deve ser válido e ter entre 10 e 120 caracteres.
 * @param cpf CPF do usuário, não pode ser nulo ou vazio, deve ser válido e conter apenas números.
 * @param password senha de acesso do usuário, não pode ser nula ou vazia, deve ter entre 8 e 30 caracteres e conter letra maiúscula e minúscula, número e caracteres especiais.
 * @param extensionNumber ramal para contato interno com o usuário, não pode ser nulo ou vazio e deve ter entre 4 e 6 caracteres.
 * @param active atividade do usuário, não pode ser nula.
 * @param nameRole nome do nível de acesso do usuário, não pode ser nulo ou vazio.
 */
public record CreateUser(
        @NotBlank(message = "O nome do usuário não deve ser nulo e nem vazio!")
        @Size(  min = 3,
                max = 100,
                message = "O nome do usuário deve ter entre 3 e 100 caracteres!")
        String name,

        @NotBlank(message = "O e-mail do usuário não deve ser nulo e nem vazio!")
        @Email(message = "Deve ser um e-mail válido!")
        @Size(  min = 10,
                max = 120,
                message = "O email do usuário deve ter entre 10 e 120 caracteres!")
        String email,

        @NotBlank(message = "O CPF do usuário não deve ser nulo e nem vazio!")
        @CPF(message = "Deve ser um CPF válido!")
        @Pattern(regexp = "\\d{11}",
                 message = "O CPF do usuário deve conter apenas números!")
        String cpf,

        @NotBlank(message = "A senha do usuário não deve ser nula e nem vazia!")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                 message = "A senha do usuário deve conter letras maiúsculas e minúsculas, números e caracteres especiais!")
        @Size(  min = 8,
                max = 30,
                message = "A senha do usuário deve ter entre 8 e 30 caracteres!")
        String password,

        @NotBlank(message = "O ramal do usuário não deve ser nulo e nem vazio!")
        @Size(  min = 4,
                max = 6,
                message = "O ramal do usuário deve ter entre 4 e 6 caracteres!")
        String extensionNumber,

        @NotNull(message = "A atividade do usuário não deve ser nula e nem vazia!")
        Boolean active,

        @NotBlank(message = "A permissão do usuário não deve ser nula e nem vazia!")
        String nameRole
) {
}
