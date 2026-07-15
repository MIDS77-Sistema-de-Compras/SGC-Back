package net.centroweg.gerenciamentocompras.modules.auth.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Lançada quando uma operação de impersonação não é permitida —
 * por exemplo, tentar logar na conta de outro administrador, de um
 * usuário inativo ou encerrar uma impersonação inexistente.
 *
 * <p>Resulta em uma resposta HTTP 403 (Forbidden).</p>
 */
public class ImpersonationNotAllowedException extends BusinessException {
    public ImpersonationNotAllowedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
