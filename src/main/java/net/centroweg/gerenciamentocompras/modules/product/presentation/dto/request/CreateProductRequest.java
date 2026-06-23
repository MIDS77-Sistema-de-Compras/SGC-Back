package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de requisição para criação de um novo produto.
 *
 * <p>Utilizado como corpo da requisição ({@code @RequestBody}) no endpoint
 * {@code POST /products}. Os campos são validados automaticamente pelo Bean Validation
 * antes de chegarem ao serviço.</p>
 *
 * <p>Exemplo de payload JSON:</p>
 * <pre>{@code
 * {
 *   "name": "Parafuso M8",
 *   "description": "Parafuso sextavado galvanizado",
 *   "price": 0.75,
 *   "type": "Insumo",
 *   "code": "PRD-00123"
 * }
 * }</pre>
 *
 * @param name        nome do produto; não pode ser nulo nem vazio
 * @param description descrição detalhada do produto; campo opcional, pode ser {@code null}
 * @param price       preço unitário do produto em reais (BRL); não pode ser {@code null}
 * @param type        tipo ou categoria do produto; não pode ser nulo nem vazio
 * @param code        código identificador único do produto (SKU); não pode ser nulo nem vazio
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
 */

public record CreateProductRequest(

            @NotBlank
            String name,

            String description,

            @NotNull
            Double price,

            @NotBlank
            String type,

            @NotBlank
            String code

) {}