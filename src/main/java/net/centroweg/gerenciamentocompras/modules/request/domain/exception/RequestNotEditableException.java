package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestNotEditableException extends BusinessException {
    public RequestNotEditableException() {
        super("Solicitação não pode ser editada após aprovação.", HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
