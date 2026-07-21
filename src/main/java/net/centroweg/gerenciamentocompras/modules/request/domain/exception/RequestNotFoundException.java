package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Exceção lançada quando uma {@link Request} não é encontrada no banco de dados.
 */
public class RequestNotFoundException extends BusinessException {
    public RequestNotFoundException() {
        super("Solicitação não encontrada!", HttpStatus.NOT_FOUND);
    }
}
