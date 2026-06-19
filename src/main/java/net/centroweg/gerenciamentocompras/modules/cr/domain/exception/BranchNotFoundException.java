package net.centroweg.gerenciamentocompras.modules.cr.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando uma {@link net.centroweg.gerenciamentocompras.modules.cr.domain.Branch}
 * não é encontrada no banco de dados.
 *
 * <p>Retorna HTTP 404 (Not Found) ao ser tratada pelo handler global de exceções.</p>
 *
 * @author Leandro
 */
public class BranchNotFoundException extends BusinessException {

    /**
     * Cria a exceção com mensagem padrão "Branch não encontrada" e status HTTP 404.
     */
    public BranchNotFoundException(){
        super("Branch não encontrada", HttpStatus.NOT_FOUND);
    }
}
