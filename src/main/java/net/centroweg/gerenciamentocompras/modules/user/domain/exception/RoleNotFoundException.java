package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * Exceção lançada quando nenhum {@link Role} é encontrado.
 */

public class RoleNotFoundException extends BusinessException {
    public RoleNotFoundException() {
        super("Nível de acesso não encontrado!", HttpStatus.NOT_FOUND);
    }

    public RoleNotFoundException(Long id) {
        super("Nível de acesso não encontrado com o id: " + id, HttpStatus.NOT_FOUND);
    }

    public RoleNotFoundException(String name) {
        super("Nível de acesso não encontrado com o nome: " + name, HttpStatus.NOT_FOUND);
    }

}