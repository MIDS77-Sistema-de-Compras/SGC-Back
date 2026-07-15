package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.SystemRole;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.ChangeUserActivationStatus;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.authorization.UserRoleAuthorizationService;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Caso de uso responsável por alterar o estado de ativação de um usuário.
 */
@Service
@RequiredArgsConstructor
public class ChangeUserActivationStatusImpl {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleAuthorizationService authorizationService;

    /**
     * Altera o estado de ativação do usuário informado.
     *
     * @param userId identificador único do usuário
     * @param request novo estado de ativação
     * @return usuário com o estado atualizado
     */
    @Transactional
    public UserResponse changeActivationStatus(
            Long userId,
            ChangeUserActivationStatus request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        authorizationService.validateCanChangeActivationStatus(
                SystemRole.from(user.getRole().getName())
        );

        return userMapper.toDTO(updateActiveStatus(user, request.active()));
    }

    /**
     * Altera o estado pela API pública do módulo, reutilizando a mesma mutação usada pelo endpoint.
     * A autorização do ator é responsabilidade do caso de uso chamador, pois schedulers não possuem
     * um usuário autenticado no contexto de segurança.
     */
    @Transactional
    public void changeActivationStatusFromPublicApi(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        updateActiveStatus(user, active);
    }

    private User updateActiveStatus(User user, boolean active) {
        user.setActive(active);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
}
