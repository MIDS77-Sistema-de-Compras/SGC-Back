package net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response;

import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;

/**
 * DTO de saída com os dados de um {@link Product}.
 * @param id identificador do produto.
 * @param name nome do produto.
 * @param description descrição detalhada do produto.
 * @param price preço unitário do produto em reais(BRL).
 * @param type tipo ou categoria do produto.
 * @param code código único do produto.
 */
public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price,
        String type,
        String code
) {}

