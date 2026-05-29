package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListRoleImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    public List<RoleResponse> listRole(){
        return mapper.toDTOList(repository.findAll());
    }
}
