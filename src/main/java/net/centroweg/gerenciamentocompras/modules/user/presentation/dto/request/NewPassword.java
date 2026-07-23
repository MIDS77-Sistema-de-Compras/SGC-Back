package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * DTO de entrada para atualização da senha de um {@link User}.
 * @param password senha de acesso do usuário, não pode ser nula ou vazia e deve ter no mínimo 8 caracteres.
 */
public record NewPassword(
        @NotBlank(message = "A senha não deve ser nula e nem vazia!")
        @Size(min=8, message = "A senha deve conter ao menos 8 caracteres!")
        String password
) {}
