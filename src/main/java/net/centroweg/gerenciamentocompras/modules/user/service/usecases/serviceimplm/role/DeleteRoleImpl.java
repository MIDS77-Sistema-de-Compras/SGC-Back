package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import org.springframework.stereotype.Service;

/** Classe de gerenciamento de exclusão de role */

@Service
@RequiredArgsConstructor
public class DeleteRoleImpl {

    private final RoleRepository repository;

    /**
     * Exclui determinada role do sistema
     * @param id ID da role a ser excluída
     */
    public void deleteRole(Long id){
        repository.deleteById(id);
    }
}
