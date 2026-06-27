package net.centroweg.gerenciamentocompras.modules.auth.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Classe que transmite mensagem de excessão ao ser chamada e acontecer algo errado.
 */

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
