package net.centroweg.gerenciamentocompras.modules.product.service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final CreateProductService createProductService;
    private final FindProductService findProductService;
    private final UpdateProductService updateProductService;
    private final DeleteProductService deleteProductService;

    @Override
    public ProductResponse create(CreateProductRequest request) {
        return createProductService.execute(request);
    }

    @Override
    public ProductResponse findById(Long id) {
        return findProductService.findById(id);
    }

    @Override
    public List<ProductResponse> findAll(String name) {
        if (name != null) {
            return findProductService.findByName(name);
        }

        return findProductService.findAll();
    }

    @Override
    public ProductResponse update(Long id, UpdateProductRequest request) {
        return updateProductService.execute(id, request);
    }

    @Override
    public void delete(Long id) {
        deleteProductService.execute(id);
    }

}