package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;

/**
 * DTO de entrada para criação e atualização de um {@link Status}.
 * @param name nome do status, não pode ser nulo ou vazio e deve respeitar o tamanho definido.
 * @param description descrição do status, não pode ser nulo ou vazio e deve respeitar o tamanho definido.
 */
public record StatusRequest(
        @NotBlank(message = "O nome do status não deve ser nulo e nem vazio!")
        @Size(min = 2, max = 25, message = "O nome deve conter entre 2 e 25 caracteres!")
        String name,

        @NotBlank(message = "A descrição do status não deve ser nula e nem vazia!")
        @Size(min = 10, max = 100, message = "A descrição deve conter entre 10 e 100 caracteres!")
        String description
) {
}
