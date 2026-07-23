package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Exceção lançada quando já existe um {@link User} com o dado informado.
 */
public class UserAlreadyExistedException extends BusinessException {
    public UserAlreadyExistedException(String variavel){
        super("Já existe um usuário com esse dado: " + variavel, HttpStatus.NOT_FOUND);
    }
}
