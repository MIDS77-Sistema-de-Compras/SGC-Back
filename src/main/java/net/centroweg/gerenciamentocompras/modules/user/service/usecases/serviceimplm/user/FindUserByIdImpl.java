package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Caso de uso responsável por buscar um {@link User} pelo seu identificador.
 */
@Service
@RequiredArgsConstructor
public class FindUserByIdImpl {

    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Busca um usuário no banco de dados pelo ID informado.
     * @param id identificador do usuário.
     * @return usuário encontrado, caso exista.
     * @throws UserNotFoundException caso nenhum usuário seja encontrado.
     */
    public UserResponse findUserById(Long id){
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }
}
