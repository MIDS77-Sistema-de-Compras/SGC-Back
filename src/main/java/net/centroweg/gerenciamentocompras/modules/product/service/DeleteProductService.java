package net.centroweg.gerenciamentocompras.modules.product.service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductService {

    private final ProductRepository productRepository;

    public void execute(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
    }

}