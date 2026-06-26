package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para criação e atualização de uma {@link net.centroweg.gerenciamentocompras.modules.cr.domain.Branch}.
 *
 * @param name nome da branch; não pode ser vazio ou conter apenas espaços
 * @author Leandro
 */
public record BranchRequest (
        @NotBlank String name
){
}
