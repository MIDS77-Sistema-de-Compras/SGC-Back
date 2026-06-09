package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateRoleImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    public RoleResponse updateRole(Long id, CreateRole role){
        Role roleSave = repository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException());

        roleSave.setName(role.name());
        return mapper.toDTO(repository.save(roleSave));
    }
}
