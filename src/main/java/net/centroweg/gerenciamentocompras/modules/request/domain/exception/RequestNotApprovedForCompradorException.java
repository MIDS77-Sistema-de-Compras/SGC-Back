package net.centroweg.gerenciamentocompras.modules.request.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

// 💩
public class RequestNotApprovedForCompradorException extends BusinessException {
    public RequestNotApprovedForCompradorException() {
        super("O comprador só pode acessar solicitações com status Aprovado.", HttpStatus.FORBIDDEN);
    }
}
