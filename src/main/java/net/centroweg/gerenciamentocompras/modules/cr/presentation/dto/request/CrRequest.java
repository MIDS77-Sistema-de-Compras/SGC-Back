package net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para criação e atualização de um Centro de Resultado (CR).
 *
 * @param name   nome do CR; não pode ser vazio
 * @param code   código identificador do CR; não pode ser vazio
 * @param master indica se este CR é o master da estrutura organizacional
 */
public record CrRequest(
        @NotBlank(message = "O nome do CR é obrigatório") String name,
        @NotBlank(message = "O código do CR é obrigatório") String code,
        @Size(max=255, message = "A descrição da CR não pode ter mais de 255 caractéres.") String description,
        Boolean master,
        String sectorName
) {
}
