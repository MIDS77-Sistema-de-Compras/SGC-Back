package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response;

/**
 * DTO de resposta para operações relacionadas a produtos.
 *
 * <p>Utilizado como corpo da resposta nos endpoints de
 * {@code POST /products}, {@code GET /products}, {@code GET /products/{id}} e
 * {@code PUT /products/{id}}, encapsulando os dados
 * da entidade {@link net.centroweg.gerenciamentocompras.modules.product.domain.Product}
 * retornados ao cliente.</p>
 *
 * <p>Exemplo de payload JSON retornado:</p>
 * <pre>{@code
 * {
 *   "id": 1,
 *   "name": "Parafuso M8",
 *   "description": "Parafuso sextavado galvanizado",
 *   "price": 0.75,
 *   "type": "Insumo",
 *   "code": "PRD-00123"
 * }
 * }</pre>
 *
 * @param id          identificador único do produto gerado pelo banco de dados
 * @param name        nome do produto
 * @param description descrição detalhada do produto; pode ser {@code null}
 *                    caso não tenha sido informada no cadastro
 * @param price       preço unitário do produto em reais (BRL)
 * @param type        tipo ou categoria do produto
 * @param code        código identificador único do produto (SKU)
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
 */
public record ProductResponse(

            Long id,
            String name,
            String description,
            Double price,
            String type,
            String code

) {}

