package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Excessão lançada quando o usuário não é encontrado no sistema.
 */

public class UserNotFoundException extends BusinessException {

    /**
     * Cria uma excessão personalizada falando que o usuário com o id não foi encontrado.
     * @param id é o id que não foi encontrado, ele é passado junto com a mensagem
     */

    public UserNotFoundException(Long id) {
        super("Usuário não encontrado com id: " + id, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String nome) {
        super("Usuário não encontrado com nome: " + nome, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException() {
        super("Usuário não encontrado.", HttpStatus.NOT_FOUND);
    }

}