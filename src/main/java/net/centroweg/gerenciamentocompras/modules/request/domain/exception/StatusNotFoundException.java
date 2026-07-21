package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;

/**
 * Exceção lançada quando um {@link Status} não é encontrado no banco de dados.
 */
public class StatusNotFoundException extends BusinessException {

    public StatusNotFoundException() {
        super("Status não encontrado!", HttpStatus.NOT_FOUND);
    }
}
