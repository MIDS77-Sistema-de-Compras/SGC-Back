package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestNotEditableException extends BusinessException {
    public RequestNotEditableException() {
        super("A solicitação não pode ser editada depois de passar pela análise.", HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
