package net.centroweg.gerenciamentocompras.modules.product.service.usecases.functionality.product;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;

/**
 * Caso de uso responsável por deletar um {@link Product}.
 */
@Service
@RequiredArgsConstructor
public class DeleteProductService {

    private final ProductRepository productRepository;

    /**
     * Deleta um produto do banco de dados.
     * @param id identificador do produto.
     */
    public void execute(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
    }

}