package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Lançada quando um CR Master não possui exatamente um coordenador ativo como responsável.
 */
public class InvalidCrMasterResponsibleException extends BusinessException {

    public InvalidCrMasterResponsibleException() {
        super(
                "Um CR Master deve possuir exatamente um COORDENADOR ativo como responsável.",
                HttpStatus.BAD_REQUEST
        );
    }
}
