package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
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

    public UserResponse createUser(CreateUser user){
        String encryptedPassword = passwordEncoder.encode(user.password());
        String encryptedCPF = passwordEncoder.encode(user.cpf());
        CreateUser userWithEncryptedPassword = new CreateUser(
                user.name(), user.email(), encryptedCPF, encryptedPassword, user.extensionNumber(), user.active(), user.nameRole(), user.profilePicture()
        );
        return mapper.toDTO(repository.save(mapper.toEntity(userWithEncryptedPassword)));
    }
}
