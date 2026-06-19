package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Lançada quando um vínculo CR-filial não é encontrado pelo seu identificador.
 *
 * <p>Resulta em uma resposta HTTP 404 (Not Found).</p>
 */
public class CrBranchNotFoundException extends BusinessException {
    public CrBranchNotFoundException(Long id) {
        super("CrBranch não encontrado com id: " + id, HttpStatus.NOT_FOUND);
    }
}
