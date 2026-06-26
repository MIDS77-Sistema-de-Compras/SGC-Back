package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.config.security.CpfHasher;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotAllowedException;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
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
    private final UserMapper mapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final CpfHasher cpfHasher;
    private final UniquenessValidator uniquenessValidator;

    /**
     * Método que cria o usuário
     * @param user DTO que traz as informações do usuário
     * @return usuário já criado
     */

    public UserResponse createUser(CreateUser user){
        uniquenessValidator.checkInfo(user);
        String encryptedPassword = passwordEncoder.encode(user.password());
        String hashedCpf = cpfHasher.hash(user.cpf());

        Role role = roleRepository.findByNameIgnoreCase(user.nameRole())
                .orElseThrow(() -> new UserNotFoundException(user.nameRole()));

        CreateUser userWithEncryptedPassword = new CreateUser(
                user.name(), user.email(), hashedCpf, encryptedPassword, user.extensionNumber(), user.active(), user.nameRole()
        );

        if(userWithEncryptedPassword.nameRole().equals("ADMIN")){
            throw new RoleNotAllowedException();
        }
        User newUser = userMapper.toEntity(userWithEncryptedPassword);

        newUser.setRole(role);

        return userMapper.toDTO(userRepository.save(newUser));
    }
}
