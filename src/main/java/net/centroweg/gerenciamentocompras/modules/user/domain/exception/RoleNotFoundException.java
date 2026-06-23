package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** Lança exceção caso a role não seja encontrada */

public class RoleNotFoundException extends BusinessException {
    public RoleNotFoundException() {
        super("Role não encontrada ", HttpStatus.NOT_FOUND);
    }

    /**
     * Lançada caso a role não seja encontrada
     * @param id ID da role
     * @throws RoleNotFoundException status NOT FOUND caso o ID da role não seja encontrado
     */
    public RoleNotFoundException(Long id) {
        super("Role não encontrada com id: " + id, HttpStatus.NOT_FOUND);
    }

    public RoleNotFoundException(String name) {
        super("Role não encontrada com nome: " + name, HttpStatus.NOT_FOUND);
    }

}