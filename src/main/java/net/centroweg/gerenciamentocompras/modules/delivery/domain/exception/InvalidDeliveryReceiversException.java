package net.centroweg.gerenciamentocompras.modules.delivery.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidDeliveryReceiversException extends BusinessException {
    public InvalidDeliveryReceiversException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
