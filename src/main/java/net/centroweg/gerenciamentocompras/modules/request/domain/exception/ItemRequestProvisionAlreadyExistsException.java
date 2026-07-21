package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import org.springframework.http.HttpStatus;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;

public class ItemRequestProvisionAlreadyExistsException extends BusinessException {

    public ItemRequestProvisionAlreadyExistsException() {
        super("Este servi\u00e7o j\u00e1 foi adicionado \u00e0 solicita\u00e7\u00e3o.", HttpStatus.CONFLICT);
    }
}
