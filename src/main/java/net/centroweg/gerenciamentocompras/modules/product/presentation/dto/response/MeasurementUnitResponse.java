package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response;
/**
 * DTO responsável pelo retorno de dados
 * de unidades de medida.
 *
 * @author Ana Beatriz de Oliveira Ribeiro
 * @since 2026
 */
public record MeasurementUnitResponse(

    Long id,
    String name,
    String abbreviation

) {
}
