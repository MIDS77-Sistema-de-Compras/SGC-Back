package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO responsável pelo recebimento de dados
 * para cadastro e atualização de unidades de medida.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */
public record MeasurementUnitRequest(

    @NotBlank(message = "O nome da unidade de medida é obrigatório.")
    @Size(min = 2, max = 50,
          message = "O nome deve ter entre 2 e 50 caracteres.")
    String name,

    @NotBlank(message = "A abreviação (sigla) é obrigatória.")
    @Size(min = 1, max = 10,
          message = "A abreviação deve ter entre 1 e 10 caracteres.")
    String abbreviation
) {
}