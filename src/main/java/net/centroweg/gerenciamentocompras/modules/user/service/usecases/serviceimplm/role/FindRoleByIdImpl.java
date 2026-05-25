package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;

@RequiredArgsConstructor
public class FindRoleByIdImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    public RoleResponse findRoleById(Long id){
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role não encontrado!")));
    }
}
