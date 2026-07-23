package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * Caso de uso responsável por buscar um {@link Role} pelo seu identificador.
 */
@Service
@RequiredArgsConstructor
public class FindRoleByIdImpl {

    private final RoleMapper mapper;
    private final RoleRepository repository;

    /**
     * Busca um nível de acesso no banco de dados pelo ID informado.
     * @param id identificador do nível de acesso.
     * @return nível de acesso encontrado, caso exista.
     * @throws RoleNotFoundException caso nenhum nível de acesso seja encontrado.
     */
    public RoleResponse findRoleById(Long id){
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id)));
    }
}
