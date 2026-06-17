package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StatusRequest(

        @NotBlank(message = "O nome não pode ser nulo.")
        @Size(min = 2, max = 25, message = "O nome deve ter entre 2 e 25 caracteres.")
        String name,

        @NotBlank(message = "O status deve conter uma descrição.")
        @Size(min = 10, max = 100, message = "A descrição deve conter entre 10 e 100 caracteres.")
        String description

) {
}
