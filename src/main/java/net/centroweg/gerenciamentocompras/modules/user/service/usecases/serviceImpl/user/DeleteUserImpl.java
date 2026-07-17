package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.SystemRole;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.authorization.UserRoleAuthorizationService;
import org.springframework.stereotype.Service;

/**
 * Deletar usuário
 */

@Service
@RequiredArgsConstructor
public class DeleteUserImpl {

    /**
     * Injeção de dependências
     */

    private final UserRepository userRepository;
    private final UserRoleAuthorizationService authorizationService;

    /**
     * Método que deleta usuário
     * @param id identificação única do usuário
     */

    public void deleteUser(Long id){

        User userSearched = userRepository.findById(id)
                .orElseThrow( UserNotFoundException::new);

        authorizationService.validateCanDeactivate(
                SystemRole.from(userSearched.getRole().getName())
        );

        userSearched.setActive(false);

        userRepository.save(userSearched);
    }
}
