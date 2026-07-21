package net.centroweg.gerenciamentocompras.modules.product.service.mapper;

import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import org.springframework.stereotype.Component;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;

/**
 * Componente responsável pela conversão entre a entidade({@link Product}) e seus DTOs de entrada({@link UpdateProductRequest} e {@link CreateProductRequest}) e de saída({@link ProductResponse}).
 */
@Component
public class ProductMapperImpl implements IProductMapper {

    /**
     * Converte uma entidade produto em um DTO de saída do produto.
     * @param product entidade com os dados do produto.
     * @return dados convertidos para DTO de saída.
     */
    @Override
    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getType(),
                product.getCode()
        );
    }

    /**
     * Converte uma lista de entidades de produto em uma lista de DTOs de saída do produto.
     * @param products lista de entidades com dados do produto.
     * @return dados convertidos para uma lista de DTOs de saída.
     */
    @Override
    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream().map(this::toResponse).toList();
    }
}