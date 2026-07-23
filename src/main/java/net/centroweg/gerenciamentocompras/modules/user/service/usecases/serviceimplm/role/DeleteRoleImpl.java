package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * Caso de uso responsável por remover um {@link Role}.
 */
@Service
@RequiredArgsConstructor
public class DeleteRoleImpl {

    private final RoleRepository repository;

    /**
     * Remove um nível de acesso do banco de dados.
     * @param id identificador do nível de acesso.
     */
    public void deleteRole(Long id){
        repository.deleteById(id);
    }
}
