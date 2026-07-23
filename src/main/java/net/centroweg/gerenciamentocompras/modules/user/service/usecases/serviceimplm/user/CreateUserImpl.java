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
 * Caso de uso responsável pela criação de um {@link User}.
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
     * Cria e persiste um novo usuário no banco de dados.
     * @param user dados do usuário.
     * @return usuário criado.
     * @throws UserNotFoundException caso nenhum nível de acesso seja encontrado.
     * @throws RoleNotAllowedException caso o nível de acesso informado seja do tipo ADMIN.
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
