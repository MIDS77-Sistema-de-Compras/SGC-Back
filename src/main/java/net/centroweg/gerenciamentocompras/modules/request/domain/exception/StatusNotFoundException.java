package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um status não é encontrado.
 *
 * <p>Essa exceção herda de {@link BusinessException} e
 * retorna o status HTTP {@link HttpStatus#NOT_FOUND}.</p>
 *
 * @author André
 * @since 1.0
 */
public class StatusNotFoundException extends BusinessException {

    /**
     * Cria uma nova exceção informando que o status não foi encontrado.
     */
    public StatusNotFoundException() {
        super("Status não encontrado!", HttpStatus.NOT_FOUND);
    }
}
