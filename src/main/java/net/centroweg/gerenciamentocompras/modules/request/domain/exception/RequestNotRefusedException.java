package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Exceção lançada quando se tenta adicionar feedback a uma {@link Request} que não foi recusada.
 */
public class RequestNotRefusedException extends BusinessException {
    public RequestNotRefusedException() {
        super("A solicitação não foi recusada, não é possível adicionar um feedback!", HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
