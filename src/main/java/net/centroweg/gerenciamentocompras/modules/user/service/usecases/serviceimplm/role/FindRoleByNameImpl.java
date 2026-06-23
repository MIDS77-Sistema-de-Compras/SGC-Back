package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/** Classe de gerenciamento de busca de role por nome */

@Service
@RequiredArgsConstructor
public class FindRoleByNameImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    /**
     * Busca roles contendo o nome pesquisado
     * @param name nome pesquisado
     * @return lista com todas as roles correspondentes ao nome
     * @see RoleResponse
     */
    public RoleResponse findRoleByName(String name) {
        return mapper.toDTO(repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RoleNotFoundException(name)));
    }
}
