package net.centroweg.gerenciamentocompras.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BusinessException(String message,HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
