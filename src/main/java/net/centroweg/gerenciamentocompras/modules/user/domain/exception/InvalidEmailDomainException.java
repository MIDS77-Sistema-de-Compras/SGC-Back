package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando o e-mail informado não pertence a um domínio institucional permitido.
 */
public class InvalidEmailDomainException extends BusinessException {
    public InvalidEmailDomainException(String allowedDomains){
        super("O e-mail deve ser institucional. Domínios permitidos: " + allowedDomains, HttpStatus.BAD_REQUEST);
    }
}
