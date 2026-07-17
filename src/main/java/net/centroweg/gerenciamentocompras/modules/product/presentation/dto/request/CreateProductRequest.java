package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;

/**
 * DTO de entrada para criação de um {@link Product}.
 * @param name nome do produto, não pode ser nulo ou vazio.
 * @param description descrição detalhada do produto.
 * @param price preço unitário do produto em reais(BRL), não pode ser nulo ou vazio e deve ser positivo.
 * @param type tipo ou categoria do produto, não pode ser nulo ou vazio.
 * @param code código único do produto, não pode ser nulo ou vazio.
 */
public record CreateProductRequest(
            @NotBlank(message = "O nome do produto não deve ser nulo e nem vazio!")
            String name,
            String description,
            @NotNull(message = "O preço do produto não deve ser nulo e nem vazio!")
            @Positive(message = "O preço do produto deve ser maior ou igual a zero!")
            Double price,
            @NotBlank(message = "O tipo ou categoria do produto não deve ser nulo e nem vazio!")
            String type,
            @NotBlank(message = "O código único do produto não deve ser nulo e nem vazio!")
            String code
) {
}