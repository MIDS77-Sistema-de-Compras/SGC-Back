package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;

/**
 * DTO de entrada para criação e atualização de um {@link Cr}.
 * @param name nome do CR, não deve ser nulo ou vazio.
 * @param code código identificador do CR, não deve ser nulo ou vazio.
 * @param master booleano que indica se este CR é o master da estrutura organizacional ou não.
 * @param sectorName nome do bloco que o CR pertence, não deve ser nulo ou vazio.
 */
public record CrRequest(
        @NotBlank(message = "O nome do CR não deve ser nulo e nem vazio!")
        String name,
        @NotBlank(message = "O código do CR não deve ser nulo e nem vazio!")
        String code,
        Boolean master,
        @NotBlank(message = "O nome do bloco não deve ser nulo e nem vazio!")
        String sectorName
) {
}
