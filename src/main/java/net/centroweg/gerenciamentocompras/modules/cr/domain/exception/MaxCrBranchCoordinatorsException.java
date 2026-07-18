package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Lançada ao tentar vincular mais coordenadores que o permitido a um CR-filial.
 */
public class MaxCrBranchCoordinatorsException extends BusinessException {

    public MaxCrBranchCoordinatorsException(int maxAllowed) {
        super(
                "O CR-filial pode possuir no máximo " + maxAllowed + " coordenador responsável.",
                HttpStatus.CONFLICT
        );
    }
}
