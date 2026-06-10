package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends BusinessException {
    public RoleNotFoundException() {
        super("Role não encontrada ", HttpStatus.NOT_FOUND);
    }

    public RoleNotFoundException(Long id) {
        super("Role não encontrada com id: " + id, HttpStatus.NOT_FOUND);
    }

    public RoleNotFoundException(String name) {
        super("Role não encontrada com nome: " + name, HttpStatus.NOT_FOUND);
    }

    public RoleNotFoundException(Long id) {
        super("Role não encontrada com id: " + id, HttpStatus.NOT_FOUND);
    }
}
