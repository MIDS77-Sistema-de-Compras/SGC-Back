package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePassword(
    @NotBlank(message = "O campo de senha antiga deve ser preenchido.")
    String oldPassword,

    @NotBlank(message = "O campo de nova senha deve ser preenchido.")
    @Size(min = 8, message = "A senha deve conter ao menos 8 caracteres.")
    String newPassword
) {}
