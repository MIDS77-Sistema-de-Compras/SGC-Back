package net.centroweg.gerenciamentocompras.modules.product.service;

import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;

import java.util.List;

public interface IProductService {

    ProductResponse create(CreateProductRequest request);

    ProductResponse findById(Long id);

    List<ProductResponse> findAll(String name);

    ProductResponse update(Long id, UpdateProductRequest request);

    void delete(Long id);

}