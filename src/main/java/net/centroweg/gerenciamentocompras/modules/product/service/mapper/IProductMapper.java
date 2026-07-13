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
     * @param product entidade com os dados.
     * @return DTO de saída com os dados do produto já convertidos.
     */
    ProductResponse toResponse(Product product);

    /**
     * Converte uma lista de entidades de produtos em uma lista de DTOs de saída dos produtos.
     * @param products lista de entidades com dados.
     * @return lista de DTOs de saída com os dados da lista de produtos já convertidos.
     */
    List<ProductResponse> toResponseList(List<Product> products);

}
