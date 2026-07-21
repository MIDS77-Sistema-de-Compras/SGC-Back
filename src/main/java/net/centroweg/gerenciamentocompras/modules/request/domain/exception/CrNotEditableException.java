package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * Exceção lançada quando se tenta alterar o CR de uma {@link Request} que já saiu do status "EM ANÁLISE".
 */
public class CrNotEditableException extends BusinessException {

    public CrNotEditableException() {
        super("O CR da solicitação não pode ser alterado pois saiu do status 'EM ANÁLISE'!", HttpStatus.UNPROCESSABLE_CONTENT);
    }
}

