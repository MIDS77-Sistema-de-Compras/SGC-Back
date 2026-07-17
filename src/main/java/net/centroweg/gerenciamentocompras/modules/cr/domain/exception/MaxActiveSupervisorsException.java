package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Lançada ao tentar vincular mais supervisores ativos a um CR
 * do que o máximo permitido.
 *
 * <p>Resulta em uma resposta HTTP 409 (Conflict).</p>
 */
public class MaxActiveSupervisorsException extends BusinessException {
    public MaxActiveSupervisorsException(int maxAllowed) {
        super("O CR já possui o número máximo de supervisores ativos permitido (" + maxAllowed + ").",
                HttpStatus.CONFLICT);
    }
}
