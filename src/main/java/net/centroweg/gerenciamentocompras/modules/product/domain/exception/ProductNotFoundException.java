package net.centroweg.gerenciamentocompras.modules.product.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;

/**
 * Exceção lançada quando um {@link Product} não é encontrado no banco de dados.
 */
public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException(Long id) {
        super("Produto não encontrado com id: " + id, HttpStatus.NOT_FOUND);
    }

    public ProductNotFoundException() {
        super("Produto não encontrado!", HttpStatus.NOT_FOUND);
    }

}
