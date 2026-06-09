package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.config.security.CpfHasher;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotAllowedException;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserImpl {

    private final UserMapper mapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final CpfHasher cpfHasher;

    public UserResponse createUser(CreateUser user){
        String encryptedPassword = passwordEncoder.encode(user.password());
        String hashedCpf = cpfHasher.hash(user.cpf(), encryptedPassword);

        CreateUser userWithEncryptedPassword = new CreateUser(
                user.name(), user.email(), hashedCpf, encryptedPassword, user.extensionNumber(), user.active(), user.nameRole()
        );
      
      if(userWithEncryptedPassword.nameRole().equals("ADMIN")){
            throw new RoleNotAllowedException();
        }
        User newUser = mapper.toEntity(userWithEncryptedPassword);

        newUser.setRole(role);

        return mapper.toDTO(repository.save(mapper.toEntity(userWithEncryptedPassword)));
    }
}
