package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.SystemRole;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.UpdateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.authorization.UserRoleAuthorizationService;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Atualizar usuário com o identificador único informado
 */


@Service
@RequiredArgsConstructor
public class UpdateUserAllImpl {

    /**
     * Injeção de dependências
     */

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleAuthorizationService authorizationService;

    /**
     * Método que atualiza usuário com o identificador único
     * @param user dados do usuário
     * @param id identificador único do usuário
     * @return usuário já atualizado
     */

    public UserResponse updateUserAll(Long id, UpdateUser user){
        User userSave = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        SystemRole currentTargetRole = SystemRole.from(userSave.getRole().getName());
        SystemRole newTargetRole = SystemRole.from(user.nameRole());
        authorizationService.validateCanEdit(currentTargetRole);
        authorizationService.validateCanCreate(newTargetRole);

        Role role = roleRepository.findByNameIgnoreCase(newTargetRole.name())
                .orElseThrow(() -> new RoleNotFoundException(user.nameRole()));
        String encryptedPassword = passwordEncoder.encode(user.password());

        userSave.setName(user.name());
        userSave.setEmail(user.email());
        userSave.setPassword(user.password());
        userSave.setExtensionNumber(user.extensionNumber());
        userSave.setActive(user.active());
        userSave.setUpdatedAt(LocalDateTime.now());
        userSave.setRole(role);

        return userMapper.toDTO(userRepository.save(userSave));
    }
}
