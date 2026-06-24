package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewPassword(
    @NotBlank(message = "O campo da senha não pode estar em branco.")
    @Size(min=8, message = "A senha deve conter ao menos 8 caracteres.")
    String password
) {}
