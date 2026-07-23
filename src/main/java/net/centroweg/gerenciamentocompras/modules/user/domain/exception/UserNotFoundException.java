package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Exceção lançada quando nenhum {@link User} é encontrado.
 */
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(Long id) {
        super("Usuário não encontrado com o id: " + id, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String nome) {
        super("Usuário não encontrado com o nome: " + nome, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException() {
        super("Usuário não encontrado!", HttpStatus.NOT_FOUND);
    }

}