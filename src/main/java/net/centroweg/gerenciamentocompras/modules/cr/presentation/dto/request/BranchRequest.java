package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;

/**
 * DTO de entrada para criação e atualização de uma {@link Branch}.
 * @param name nome da branch, não pode ser nulo ou vazio.
 */
public record BranchRequest (
        @NotBlank(message = "O nome não da branch não deve ser nulo e nem vazio!")
        String name
){
}
