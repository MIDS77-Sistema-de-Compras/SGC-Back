package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidAttachmentException extends BusinessException {

    public InvalidAttachmentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}