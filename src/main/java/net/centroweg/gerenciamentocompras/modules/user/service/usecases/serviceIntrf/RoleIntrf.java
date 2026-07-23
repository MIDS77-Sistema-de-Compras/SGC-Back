package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * Interface de serviço para operações de gerenciamento do {@link Role}.
 */
public interface RoleIntrf {

    /**
     * Cria e persiste um novo nível de acesso no banco de dados.
     * @param role dados do nível de acesso.
     * @return nível de acesso criado.
     */
    RoleResponse createRole(CreateRole role);

    /**
     * Lista todos os níveis de acesso cadastrados no banco de dados.
     * @return lista com todos os níveis de acesso encontrados, caso exista.
     */
    List<RoleResponse> listRole();

    /**
     * Busca um nível de acesso no banco de dados pelo ID informado.
     * @param id identificador do nível de acesso.
     * @return nível de acesso encontrado, caso exista.
     */
    RoleResponse findRoleById(Long id);

    /**
     * Busca um nível de acesso no banco de dados pelo nome informado.
     * @param name nome do nível de acesso.
     * @return nível de acesso encontrado, caso exista.
     */
    RoleResponse findRoleByName(String name);

    /**
     * Atualiza um nível de acesso existente no banco de dados.
     * @param id identificador do nível de acesso.
     * @param role novos dados do nível de acesso.
     * @return nível de acesso já atualizado.
     */
    RoleResponse updateRole(Long id, CreateRole role);

    /**
     * Remove um nível de acesso do banco de dados.
     * @param id identificador do nível de acesso.
     */
    void deleteRole(Long id);
}
