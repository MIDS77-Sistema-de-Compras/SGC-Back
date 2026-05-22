package net.centroweg.gerenciamentocompras.modules.product.service.mapper;

import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.VariationResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapperImpl implements IProductMapper {

    @Override
    public ProductResponse toResponse(Product product) {
        List<VariationResponse> variations = product.getVariations().stream()
                .map(v -> new VariationResponse(v.getId(), v.getName()))
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getType(),
                product.getCode(),
                variations
        );
    }

    @Override
    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream().map(this::toResponse).toList();
    }

}