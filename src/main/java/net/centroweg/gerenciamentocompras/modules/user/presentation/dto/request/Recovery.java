package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Recovery(
    @NotBlank(message = "O campo de email é obrigatório.")
    @Email(message = "O campo precisa estar em formato de email.")
    String email
) {}
