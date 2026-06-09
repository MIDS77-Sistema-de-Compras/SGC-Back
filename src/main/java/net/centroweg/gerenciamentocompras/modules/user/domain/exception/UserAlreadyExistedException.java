package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistedException extends BusinessException {
    public UserAlreadyExistedException(String variavel){
        super("Já existe um usuário com este " + variavel, HttpStatus.NOT_FOUND);
    }
}
