package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando já existe um status cadastrado
 * com o mesmo nome informado.
 *
 * <p>Essa exceção herda de {@link BusinessException} e
 * retorna o status HTTP {@link HttpStatus#BAD_REQUEST}.</p>
 *
 * @author André
 * @since 1.0
 */
public class StatusAlreadyExistsException extends BusinessException {


    /**
     * Cria uma nova exceção informando que o status já existe.
     */
    public StatusAlreadyExistsException() {
        super("Já existe um status cadastrado com esse nome.", HttpStatus.BAD_REQUEST);
    }
}
