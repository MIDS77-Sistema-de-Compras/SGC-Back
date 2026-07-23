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
 * Caso de uso responsável pela atualização de um {@link User}.
 */
@Service
@RequiredArgsConstructor
public class UpdateUserAllImpl {

    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Atualiza um usuário existente no banco de dados.
     * @param id identificador do usuário.
     * @param user novos dados do usuário.
     * @return usuário já atualizado.
     * @throws UserNotFoundException caso nenhum usuário seja encontrado.
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
