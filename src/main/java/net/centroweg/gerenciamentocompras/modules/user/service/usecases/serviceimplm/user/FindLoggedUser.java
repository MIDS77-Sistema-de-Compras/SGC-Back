package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por buscar um {@link User} autenticado.
 */
@Service
@RequiredArgsConstructor
public class FindLoggedUser {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    /**
     * Busca o usuário autenticado no banco de dados.
     * @param userPrincipal dados do usuário autenticado.
     * @return usuário encontrado, caso exista.
     * @throws UserNotFoundException caso nenhum usuário seja encontrado.
     */
    public UserResponse findLoggedUser(UserPrincipal userPrincipal){

        User userSearched = userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toDTO(userSearched);
    }
}
