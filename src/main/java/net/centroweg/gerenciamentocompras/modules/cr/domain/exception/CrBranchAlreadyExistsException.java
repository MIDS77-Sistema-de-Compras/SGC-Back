package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Lançada ao tentar vincular um CR a uma filial quando esse vínculo já existe.
 *
 * <p>Resulta em uma resposta HTTP 409 (Conflict).</p>
 */
public class CrBranchAlreadyExistsException extends BusinessException {
    public CrBranchAlreadyExistsException() {
        super("CR já vinculado a esta filial", HttpStatus.CONFLICT);
    }
}
