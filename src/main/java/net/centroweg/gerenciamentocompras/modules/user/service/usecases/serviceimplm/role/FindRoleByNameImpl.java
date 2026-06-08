package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindRoleByNameImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    public RoleResponse findRoleByName(String name) {
        return mapper.toDTO(repository.findByNameIgnoringCase(name)
                .orElseThrow(() -> new RoleNotFoundException(name)));
    }
}
