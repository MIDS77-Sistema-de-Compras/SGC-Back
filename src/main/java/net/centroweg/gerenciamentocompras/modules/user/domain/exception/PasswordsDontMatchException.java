package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import org.springframework.http.HttpStatus;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;

public class PasswordsDontMatchException extends BusinessException {
    
    public PasswordsDontMatchException(){
        super("A senha antiga não confere com a senha atual.", HttpStatus.FORBIDDEN);
    }

}
