package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;

/**
 * Exceção lançada quando uma {@link Branch} não é encontrada no banco de dados.
 */
public class BranchNotFoundException extends BusinessException {

    public BranchNotFoundException(){
        super("Filial não encontrada!", HttpStatus.NOT_FOUND);
    }
}
