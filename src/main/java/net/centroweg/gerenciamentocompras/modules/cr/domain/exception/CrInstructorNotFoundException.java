package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import org.springframework.http.HttpStatus;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;

/**
 * Excessão lançada quando um vínculo CR-Instructor não é encontrado pelo seu identificador.
 */
public class CrInstructorNotFoundException extends BusinessException {
    public CrInstructorNotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }

}
