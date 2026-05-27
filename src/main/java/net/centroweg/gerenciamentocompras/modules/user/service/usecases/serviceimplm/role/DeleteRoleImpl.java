package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRoleImpl {

    private final RoleRepository repository;

    public void deleteRole(Long id){
        repository.deleteById(id);
    }
}
