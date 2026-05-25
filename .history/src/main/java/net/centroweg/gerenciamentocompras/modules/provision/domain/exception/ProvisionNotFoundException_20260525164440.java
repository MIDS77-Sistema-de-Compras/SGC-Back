package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

import org.springframework.http.HttpStatus;

public class ProvisionNotFoundException extends BusinessException {
    public ProvisionNotFoundException(String message, HttpStatus httpStatus){
        super(message);
        httpStatus = HttpStatus.NOT_FOUND;
    }
}
