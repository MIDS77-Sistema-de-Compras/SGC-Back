package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
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

    /**
     * Injeção de dependências
     */

    private final UserMapper mapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Método que cria o usuário
     * @param user DTO que traz as informações do usuário
     * @return usuário já criado
     */

    public UserResponse createUser(CreateUser user){
        String encryptedPassword = passwordEncoder.encode(user.password());
        String encryptedCPF = passwordEncoder.encode(user.cpf());
        CreateUser userWithEncryptedPassword = new CreateUser(
                user.name(), user.email(), encryptedCPF, encryptedPassword, user.extensionNumber(), user.active(), user.nameRole()
        );
        return mapper.toDTO(repository.save(mapper.toEntity(userWithEncryptedPassword)));
    }
}
