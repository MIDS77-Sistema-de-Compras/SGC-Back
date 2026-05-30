package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
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

    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Método que atualiza usuário com o identificador único
     * @param user dados do usuário
     * @param id identificador único do usuário
     * @return usuário já atualizado
     */

    public UserResponse updateUserAll(Long id, CreateUser user){
        User userSave = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userSave.setName(user.name());
        userSave.setCpf(user.cpf());
        userSave.setEmail(user.email());
        userSave.setPassword(user.password());
        userSave.setExtensionNumber(user.extensionNumber());
        userSave.setActive(user.active());
        userSave.setUpdatedAt(LocalDateTime.now());

        return mapper.toDTO(repository.save(userSave));
    }
}
