package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;

/**
 * DTO de entrada para criação e atualização de um {@link Cr}.
 * @param name nome do CR, não deve ser nulo e nem vazio.
 * @param code código identificador do CR, não deve ser nulo e nem vazio.
 * @param master booleano que indica se este CR é o master da estrutura organizacional(true) ou não(false).
 */
public record CrRequest(
        @NotBlank(message = "O nome do CR não deve ser nulo e nem vazio!")
        String name,
        @NotBlank(message = "O código do CR não deve ser nulo e nem vazio!")
        String code,
        Boolean master,
        @NotBlank(message = "O nome do setor não deve ser nulo e nem vazio!")
        String sectorName
) {
}
