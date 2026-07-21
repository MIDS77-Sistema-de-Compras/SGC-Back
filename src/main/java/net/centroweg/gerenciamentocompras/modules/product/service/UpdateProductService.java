package net.centroweg.gerenciamentocompras.modules.product.service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.IProductMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProductService {

    private final ProductRepository productRepository;
    private final IProductMapper productMapper;

    public ProductResponse execute(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        String normalizedName = normalizeName(request.name());
        if (productRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, id)) {
            throw new ProductAlreadyExistsException();
        }

        product.setName(normalizedName);
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setType(request.type());
        product.setCode(request.code());

        return productMapper.toResponse(productRepository.save(product));
    }

    private String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }
}
