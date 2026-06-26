package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.RoleIntrf;
import org.springframework.stereotype.Service;

import java.util.List;

/** Implementa interfaces contendo todas as operações possíveis para a regra de negócio */

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleIntrf {

    private final CreateRoleImpl createRole;
    private final ListRoleImpl listRole;
    private final FindRoleByIdImpl findRoleById;
    private final FindRoleByNameImpl findRoleByName;
    private final UpdateRoleImpl updateRole;
    private final DeleteRoleImpl deleteRole;

    /**
     * Reutiliza método de criação de role
     * @param role role a ser criada
     * @return response DTO de role criada
     * @see CreateRoleImpl#createRole(CreateRole)
     */
    @Override
    public RoleResponse createRole(CreateRole role) {
        return createRole.createRole(role);
    }

    /**
     * Reutiliza método de listagem geral de roles cadastradas
     * @return lista com todas as roles presentes no sistema
     * @see ListRoleImpl#listRole()
     */
    @Override
    public List<RoleResponse> listRole() {
        return listRole.listRole();
    }

    /**
     * Reutiliza método de busca de role por ID
     * @param id ID da role requisitada
     * @return response DTO de role requisitada
     * @see FindRoleByIdImpl#findRoleById(Long) 
     */
    @Override
    public RoleResponse findRoleById(Long id) {
        return findRoleById.findRoleById(id);
    }

    /**
     * Reutiliza método de busca de role contendo determinado nome
     * @param name nome pesquisado
     * @return lista com todas as roles correspondentes a pesquisa
     * @see FindRoleByNameImpl#findRoleByName(String) 
     */
    @Override
    public RoleResponse findRoleByName(String name){
        return findRoleByName.findRoleByName(name);
    }

    /**
     * Reutiliza método de atualização de role
     * @param id ID da role a ser atualizada
     * @param role novas informações para a role
     * @return responseDTO da role após atualização
     * @see UpdateRoleImpl#updateRole(Long, CreateRole) 
     */
    @Override
    public RoleResponse updateRole(Long id, CreateRole role){
        return updateRole.updateRole(id, role);
    }

    /**
     * Reutiliza método de exclusão de role específica
     * @param id ID da role a ser excluída
     * @see DeleteRoleImpl#deleteRole(Long) 
     */
    @Override
    public void deleteRole(Long id){
        deleteRole.deleteRole(id);
    }

}
