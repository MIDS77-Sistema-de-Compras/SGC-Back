package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * Encontrar usuário pelo identificador único
 */

@Service
@RequiredArgsConstructor
public class FindUserByIdImpl {

    /**
     * Injeção de dependências
     */

    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Método que busca usuário pelo identificador único
     * @param id identificador único do usuário
     * @return usuário que foi encontrado
     */

    public UserResponse findUserById(Long id){
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }
}
