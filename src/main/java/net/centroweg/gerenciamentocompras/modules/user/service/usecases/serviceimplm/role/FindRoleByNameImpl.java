package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;

import java.util.List;

@RequiredArgsConstructor
public class FindRoleByNameImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    public List<RoleResponse> findRoleByName(String name) {
        return mapper.toDTOList(repository.findByNameIgnoringCase(name));
    }
}
