package net.centroweg.gerenciamentocompras.modules.product.service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.mapper.IProductMapper;
import net.centroweg.gerenciamentocompras.shared.persistence.UniqueConstraintViolationDetector;
import net.centroweg.gerenciamentocompras.shared.util.NameNormalizer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductService {

    private final ProductRepository productRepository;
    private final IProductMapper productMapper;

    @Transactional
    public ProductResponse execute(CreateProductRequest request) {
        String normalizedName = NameNormalizer.normalize(request.name());
        if (productRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new ProductAlreadyExistsException();
        }

        Product product = Product.builder()
                .name(normalizedName)
                .description(request.description())
                .price(request.price())
                .type(request.type())
                .code(request.code())
                .build();

        try {
            Product savedProduct = productRepository.save(product);
            productRepository.flush();
            return productMapper.toResponse(savedProduct);
        } catch (DataIntegrityViolationException exception) {
            if (UniqueConstraintViolationDetector.isConstraintViolation(exception, "ux_product_normalized_name")) {
                throw new ProductAlreadyExistsException();
            }
            throw exception;
        }
    }

}
