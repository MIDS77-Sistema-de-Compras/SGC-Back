package net.centroweg.gerenciamentocompras.modules.provision.domain.exception;

import org.springframework.http.HttpStatus;

import net.centroweg.gerenciamentocompras.modules.shared.exception.BusinessException;

public class ProvisionNotFoundException extends BusinessException {
    public ProvisionNotFoundException(String message, HttpStatus httpStatus){
        super(message);
        httpStatus
    }
}
