package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * Caso de uso responsável por listar um {@link Role}.
 */
@Service
@RequiredArgsConstructor
public class ListRoleImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    /**
     * Lista todos os níveis de acesso cadastrados no banco de dados.
     * @return lista com todos os níveis de acesso encontrados, caso exista.
     */
    public List<RoleResponse> listRole(){
        return mapper.toDTOList(repository.findAll());
    }
}
