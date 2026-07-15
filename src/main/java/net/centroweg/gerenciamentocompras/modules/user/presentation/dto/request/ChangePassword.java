package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePassword(
    @NotBlank(message = "O campo de senha antiga deve ser preenchido.")
    String oldPassword,

    @NotBlank(message = "O campo de nova senha deve ser preenchido.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
             message = "A senha deve conter letra maiúscula e minúscula, número e caracteres especiais.")
    @Size(min = 8, max = 30, message = "A senha deve ter entre 8 e 30 caracteres.")
    String newPassword
) {}
