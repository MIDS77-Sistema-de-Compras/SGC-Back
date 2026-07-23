package net.centroweg.gerenciamentocompras.modules.user.domain.exception;

import net.centroweg.gerenciamentocompras.shared.exception.BusinessException;
import org.springframework.http.HttpStatus;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * Exceção lançada quando não é possível criar um usuário com o {@link Role} informado.
 */
public class RoleNotAllowedException extends BusinessException {
    public RoleNotAllowedException(){
        super("Erro, não é possível criar um user com este nível de acesso!", HttpStatus.FORBIDDEN);
    }
}
