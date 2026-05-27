package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRoleImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    public RoleResponse createRole(CreateRole role){
        return mapper.toDTO(repository.save(mapper.toEntity(role)));
    }
}
