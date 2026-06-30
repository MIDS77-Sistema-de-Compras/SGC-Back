package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;

/**
 * DTO de saída com os dados de uma {@link Branch}.
 * @param id identificador único da branch.
 * @param name nome da branch.
 */
public record BranchResponse (
        Long id,
        String name
){
}
