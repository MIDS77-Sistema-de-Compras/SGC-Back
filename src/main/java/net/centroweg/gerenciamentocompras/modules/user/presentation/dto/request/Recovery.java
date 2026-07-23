package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Recovery(
    @NotBlank(message = "O e-mail não deve ser nulo e nem vazio!")
    @Email(message = "Deve ser um e-mail válido!")
    String email
) {}
