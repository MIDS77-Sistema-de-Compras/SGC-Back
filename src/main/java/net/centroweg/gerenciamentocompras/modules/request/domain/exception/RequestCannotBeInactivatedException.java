package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Exceção lançada quando se tenta inativar uma {@link Request} que já foi aprovada.
 */
public class RequestCannotBeInactivatedException extends BusinessException {
    public RequestCannotBeInactivatedException() {
        super("Solicitação não pode ser inativada após aprovação!", HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
