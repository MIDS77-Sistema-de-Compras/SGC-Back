package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(Long id) {
        super("Usuário não encontrado com id: " + id, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String nome) {
        super("Usuário não encontrado com nome: " + nome, HttpStatus.NOT_FOUND);
    }

}