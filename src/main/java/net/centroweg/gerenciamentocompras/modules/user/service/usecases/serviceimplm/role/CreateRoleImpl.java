package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * Caso de uso responsável pela criação de um {@link Role}.
 */
@Service
@RequiredArgsConstructor
public class CreateRoleImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    /**
     * Cria e persiste um novo nível de acesso no banco de dados.
     * @param role dados do nível de acesso.
     * @return nível de acesso criado.
     */
    public RoleResponse createRole(CreateRole role){

        return mapper.toDTO(repository.save(mapper.toEntity(role)));
    }
}
