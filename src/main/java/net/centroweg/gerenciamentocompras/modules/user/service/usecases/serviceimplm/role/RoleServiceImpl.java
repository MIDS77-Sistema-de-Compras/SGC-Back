package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.RoleIntrf;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * Classe de serviço do {@link Role} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link RoleIntrf} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleIntrf {

    /**
     * Componente responsável pela criação de um nível de acesso.
     */
    private final CreateRoleImpl createRole;

    /**
     * Componente responsável por qualquer tipo de busca de nível de acesso.
     */
    private final ListRoleImpl listRole;

    /**
     * Componente responsável pela busca de um nível de acesso pelo seu identificador.
     */
    private final FindRoleByIdImpl findRoleById;

    /**
     * Componente responsável pela busca de um nível de acesso pelo seu nome.
     */
    private final FindRoleByNameImpl findRoleByName;

    /**
     * Componente responsável pela atualização de um nível de acesso.
     */
    private final UpdateRoleImpl updateRole;

    /**
     * Componente responsável por remover um nível de acesso.
     */
    private final DeleteRoleImpl deleteRole;

    /**
     * Cria e persiste um novo nível de acesso no banco de dados.
     * @param role dados do nível de acesso.
     * @return nível de acesso criado.
     */
    @Override
    public RoleResponse createRole(CreateRole role) {
        return createRole.createRole(role);
    }

    /**
     * Lista todos os níveis de acesso cadastrados no banco de dados.
     * @return lista com todos os níveis de acesso encontrados, caso exista.
     */
    @Override
    public List<RoleResponse> listRole() {
        return listRole.listRole();
    }

    /**
     * Busca um nível de acesso no banco de dados pelo ID informado.
     * @param id identificador do nível de acesso.
     * @return nível de acesso encontrado, caso exista.
     */
    @Override
    public RoleResponse findRoleById(Long id) {
        return findRoleById.findRoleById(id);
    }

    /**
     * Busca um nível de acesso no banco de dados pelo nome informado.
     * @param name nome do nível de acesso.
     * @return nível de acesso encontrado, caso exista.
     */
    @Override
    public RoleResponse findRoleByName(String name){
        return findRoleByName.findRoleByName(name);
    }

    /**
     * Atualiza um nível de acesso existente no banco de dados.
     * @param id identificador do nível de acesso.
     * @param role novos dados do nível de acesso.
     * @return nível de acesso já atualizado.
     */
    @Override
    public RoleResponse updateRole(Long id, CreateRole role){
        return updateRole.updateRole(id, role);
    }

    /**
     * Remove um nível de acesso do banco de dados.
     * @param id identificador do nível de acesso.
     */
    @Override
    public void deleteRole(Long id){
        deleteRole.deleteRole(id);
    }

}
