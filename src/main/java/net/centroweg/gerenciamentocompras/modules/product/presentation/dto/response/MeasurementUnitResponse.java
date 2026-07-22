package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;

/**
 * DTO de saída com os dados de uma {@link MeasurementUnit}.
 * @param id identificador da unidade de medida.
 * @param name nome da unidade de medida.
 * @param abbreviation abreviação(sigla) da unidade de medida.
 */
public record MeasurementUnitResponse(
    Long id,

    String name,

    String abbreviation

) {
}