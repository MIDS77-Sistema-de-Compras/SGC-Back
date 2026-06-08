package net.centroweg.gerenciamentocompras.modules.product.service.mapper;

import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;

import java.util.List;

public interface IProductMapper {

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

}
