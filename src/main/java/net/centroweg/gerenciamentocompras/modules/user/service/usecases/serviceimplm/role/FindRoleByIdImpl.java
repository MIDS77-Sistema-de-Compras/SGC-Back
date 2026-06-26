package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/** Classe de gerenciamento de busca de role por ID */

@Service
@RequiredArgsConstructor
public class FindRoleByIdImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    /**
     * Busca uma específica role com base no ID
     * @param id ID da role requisitada
     * @return role que possui o ID específico
     * @throws RoleNotFoundException caso ID não seja encontrado
     * @see RoleNotFoundException
     * @see RoleResponse
     */
    public RoleResponse findRoleById(Long id){
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id)));
    }
}
