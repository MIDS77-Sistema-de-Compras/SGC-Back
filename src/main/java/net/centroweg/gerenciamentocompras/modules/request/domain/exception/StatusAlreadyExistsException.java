package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class StatusAlreadyExistsException extends BusinessException {

    public StatusAlreadyExistsException() {
        super("Já existe um status cadastrado com esse nome.", HttpStatus.BAD_REQUEST);
    }
}
