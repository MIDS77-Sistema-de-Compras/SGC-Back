package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
/**
 * DTO de requisição para atualização de um produto existente.
 *
 * <p>Utilizado como corpo da requisição ({@code @RequestBody}) no endpoint
 * {@code PUT /products/{id}}. Os campos são validados automaticamente pelo Bean Validation
 * antes de chegarem ao serviço.</p>
 *
 * <p>Todos os campos são obrigatórios na atualização, com exceção de
 * {@code description}, que permanece opcional.</p>
 *
 * <p>Exemplo de payload JSON:</p>
 * <pre>{@code
 * {
 *   "name": "Parafuso M10",
 *   "description": "Parafuso sextavado zincado reforçado",
 *   "price": 1.25,
 *   "type": "Insumo",
 *   "code": "PRD-00124"
 * }
 * }</pre>
 *
 * @param name        nome do produto; não pode ser nulo nem vazio
 * @param description descrição detalhada do produto; campo opcional, pode ser {@code null}
 * @param price       preço unitário do produto em reais (BRL); não pode ser {@code null}
 *                    e deve ser um valor positivo maior que zero
 * @param type        tipo ou categoria do produto; não pode ser nulo nem vazio
 * @param code        código identificador único do produto (SKU); não pode ser nulo nem vazio
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
 */
public record UpdateProductRequest(

        @NotBlank
        String name,

        String description,

        @NotNull
        @Positive
        Double price,

        @NotBlank
        String type,

        @NotBlank
        String code

) {
}