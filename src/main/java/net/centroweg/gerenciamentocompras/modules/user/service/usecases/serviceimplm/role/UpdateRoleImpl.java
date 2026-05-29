package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/** Classe de gerenciamento de atualização de role */

@Service
@RequiredArgsConstructor
public class UpdateRoleImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    /**
     * Atualiza informações de role específica
     * @param id ID da role para atualizações
     * @param role DTO contendo novos dados para a role
     * @return response DTO de role atualizada
     * @throws RoleNotFoundException caso a role não seja encontrada
     * @see RoleResponse
     * @see CreateRole
     */
    public RoleResponse updateRole(Long id, CreateRole role){
        Role roleSave = repository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));

        roleSave.setName(role.name());
        return mapper.toDTO(repository.save(roleSave));
    }
}
