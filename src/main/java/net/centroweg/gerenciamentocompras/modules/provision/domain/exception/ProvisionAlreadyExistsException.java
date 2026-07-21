package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

import org.springframework.http.HttpStatus;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;

public class ProvisionAlreadyExistsException extends BusinessException {

    public ProvisionAlreadyExistsException() {
        super("J\u00e1 existe um servi\u00e7o cadastrado com esse nome.", HttpStatus.CONFLICT);
    }
}
