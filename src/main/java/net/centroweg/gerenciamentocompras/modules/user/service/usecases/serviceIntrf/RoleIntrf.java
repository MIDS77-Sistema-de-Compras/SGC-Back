package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;

import java.util.List;

public interface RoleIntrf {

    RoleResponse createRole(CreateRole role);
    List<RoleResponse> listRole();
    RoleResponse findRoleById(Long id);
    RoleResponse findRoleByName(String name);
    RoleResponse updateRole(Long id, CreateRole role);
    void deleteRole(Long id);
}
