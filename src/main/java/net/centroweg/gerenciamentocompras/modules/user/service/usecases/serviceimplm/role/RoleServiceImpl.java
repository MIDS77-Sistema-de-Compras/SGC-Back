package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.RoleIntrf;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleIntrf {

    private final CreateRoleImpl createRole;
    private final ListRoleImpl listRole;
    private final FindRoleByIdImpl findRoleById;
    private final FindRoleByNameImpl findRoleByName;
    private final UpdateRoleImpl updateRole;
    private final DeleteRoleImpl deleteRole;

    @Override
    public RoleResponse createRole(CreateRole role) {
        return createRole.createRole(role);
    }

    @Override
    public List<RoleResponse> listRole() {
        return listRole.listRole();
    }

    @Override
    public RoleResponse findRoleById(Long id) {
        return findRoleById.findRoleById(id);
    }

    @Override
    public List<RoleResponse> findRoleByName(String name){
        return findRoleByName.findRoleByName(name);
    }

    @Override
    public RoleResponse updateRole(Long id, CreateRole role){
        return updateRole.updateRole(id, role);
    }

    @Override
    public void deleteRole(Long id){
        deleteRole.deleteRole(id);
    }

}
