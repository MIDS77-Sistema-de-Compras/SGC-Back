package net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * DTO de entrada para criação de um {@link Role}.
 * @param name nome do nível de acesso, não pode ser nulo ou vazio e deve respeitar o tamanho definido.
 */
public record CreateRole(
        @NotBlank(message = "O nome do nível de acesso não deve ser nulo e nem vazio!")
        @Size(  min = 3,
                max = 100,
                message = "O nome deve ter entre 3 e 100 caracteres!")
        String name
) {
}
