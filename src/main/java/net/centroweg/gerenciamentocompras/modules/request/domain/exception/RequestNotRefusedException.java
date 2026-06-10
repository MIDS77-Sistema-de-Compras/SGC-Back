package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RequestNotRefusedException extends BusinessException {
    public RequestNotRefusedException() {
        super("A solicitação não foi recusada, não é possível adicionar feedback!", HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
