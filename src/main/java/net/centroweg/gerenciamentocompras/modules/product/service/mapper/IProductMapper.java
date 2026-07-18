package net.centroweg.gerenciamentocompras.modules.product.service.mapper;

import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import java.util.List;

/**
 * Interface que isola a camada de apresentação da camada de domínio, evitando exposição pela API.
 */
public interface IProductMapper {

    /**
     * Converte uma entidade produto em um DTO de saída do produto.
     * @param product entidade com os dados do produto.
     * @return dados convertidos para DTO de saída.
     */
    ProductResponse toResponse(Product product);

    /**
     * Converte uma lista de entidades de produto em uma lista de DTOs de saída dos produtos.
     * @param products lista de entidades com os dados do produto.
     * @return dados convertidos para uma lista de DTOs de saída.
     */
    List<ProductResponse> toResponseList(List<Product> products);

}
