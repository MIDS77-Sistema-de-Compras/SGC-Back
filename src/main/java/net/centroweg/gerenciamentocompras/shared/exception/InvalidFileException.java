package net.centroweg.gerenciamentocompras.shared.exception;

import org.springframework.http.HttpStatus;

public class InvalidFileException extends BusinessException {

    public InvalidFileException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
