package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/** Classe de gerenciamento de criação de role */

@Service
@RequiredArgsConstructor
public class CreateRoleImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;
    private final UserRepository userRepository;

    /**
     * Cria uma nova role no sistema
     * @param role role a ser criada
     * @return responde DTO de role criada
     * @see RoleMapper
     * @see RoleResponse
     * @see CreateRole
     */
    public RoleResponse createRole(CreateRole role){

        return mapper.toDTO(repository.save(mapper.toEntity(role)));
    }
}
