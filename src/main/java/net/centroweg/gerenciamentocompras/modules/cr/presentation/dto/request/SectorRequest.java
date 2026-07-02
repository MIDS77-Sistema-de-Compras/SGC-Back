package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;

/**
 * DTO de entrada para criação e atualização do {@link Sector}.
 * @param name nome do bloco, não deve ser nulo e nem vazio.
 */
public record SectorRequest(
        @NotBlank(message = "O nome do bloco não deve ser nulo e nem vazio!")
        String name
) {
}
