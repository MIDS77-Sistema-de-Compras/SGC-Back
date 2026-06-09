package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import org.springframework.http.HttpStatus;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;

public class CrInstructorNotFoundException extends BusinessException {
    public CrInstructorNotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }

}
