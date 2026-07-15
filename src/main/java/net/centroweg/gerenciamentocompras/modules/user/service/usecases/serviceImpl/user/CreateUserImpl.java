package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.config.security.CpfHasher;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.SystemRole;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.authorization.UserRoleAuthorizationService;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Criação dos usuários
 */

@Service
@RequiredArgsConstructor
public class CreateUserImpl {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CpfHasher cpfHasher;
    private final UniquenessValidator uniquenessValidator;
    private final UserRoleAuthorizationService authorizationService;

    /**
     * Método que cria o usuário
     * @param user DTO que traz as informações do usuário
     * @return usuário já criado
     */

    public UserResponse createUser(CreateUser user){
        SystemRole targetRole = SystemRole.from(user.nameRole());
        authorizationService.validateCanCreate(targetRole);

        uniquenessValidator.checkInfo(user);
        String encryptedPassword = passwordEncoder.encode(user.password());
        String hashedCpf = cpfHasher.hash(user.cpf());

        Role role = roleRepository.findByNameIgnoreCase(targetRole.name())
                .orElseThrow(() -> new UserNotFoundException(user.nameRole()));

        CreateUser userWithEncryptedPassword = new CreateUser(
                user.name(), user.email(), hashedCpf, encryptedPassword, user.extensionNumber(), user.active(), targetRole.name()
        );

        User newUser = userMapper.toEntity(userWithEncryptedPassword);

        newUser.setRole(role);

        return userMapper.toDTO(userRepository.save(newUser));
    }
}
