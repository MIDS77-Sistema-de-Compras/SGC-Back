package net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * Interface que define o contrato de obtenção do {@link Role}.
 */
public interface RoleLevels {

    /**
     * Retorna o nome do nível de acesso.
     * @return nome do nível de acesso.
     */
    String getRole();
}
