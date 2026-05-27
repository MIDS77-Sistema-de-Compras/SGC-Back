package net.centroweg.gerenciamentocompras.modules.product.service.mapper;

import net.centroweg.gerenciamentocompras.modules.product.domain.exception.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapperImpl implements IProductMapper {

    @Override
    public ProductResponse toResponse(Product product) {
        return new

                ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getType(),
                product.getCode(),
                product.getVariation()
        );

    }
    @Override
    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream().map(this::toResponse).toList();
    }
}