package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response;


/**
 * DTO de resposta para operações relacionadas à unidade de medida.
 *
 * <p>Utilizado como corpo da resposta nos endpoints de
 * {@code POST /measurement-unit}, {@code GET /measurement-unit},
 * {@code GET /measurement-unit/{id}}, {@code GET /measurement-unit/search} e
 * {@code PUT /measurement-unit/{id}}, encapsulando os dados
 * da entidade {@link net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit}
 * retornados ao cliente.</p>
 *
 * <p>Exemplo de payload JSON retornado:</p>
 * <pre>{@code
 * {
 *   "id": 1,
 *   "name": "Quilograma",
 *   "abbreviation": "kg"
 * }
 * }</pre>
 *
 * @param id           identificador único da unidade de medida gerado pelo banco de dados
 * @param name         nome completo da unidade de medida
 * @param abbreviation abreviação (sigla) da unidade de medida
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
 */
public record MeasurementUnitResponse(

    Long id, 
    String name,
    String abbreviation

) {
    
}
