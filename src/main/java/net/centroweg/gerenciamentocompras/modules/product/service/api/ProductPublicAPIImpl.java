package net.centroweg.gerenciamentocompras.modules.product.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import net.centroweg.gerenciamentocompras.modules.product.service.api.dto.ProductData;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductPublicAPIImpl implements IProductPublicAPI {

    private final ProductRepository productRepository;

    @Override
    public ProductData findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return new ProductData(
                product.getId(),
                product.getName(),
                product.getCode(),
                product.getPrice()
        );
    }
    }