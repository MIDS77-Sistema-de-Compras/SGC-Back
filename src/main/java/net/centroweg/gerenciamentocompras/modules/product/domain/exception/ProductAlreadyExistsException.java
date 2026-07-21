package net.centroweg.gerenciamentocompras.modules.product.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ProductAlreadyExistsException extends BusinessException {

    public ProductAlreadyExistsException() {
        super("Já existe um produto cadastrado com esse nome.", HttpStatus.CONFLICT);
    }
}
