package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * Deletar usuário
 */

@Service
@RequiredArgsConstructor
public class DeleteUserImpl {

    /**
     * Injeção de dependências
     */

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    /**
     * Método que deleta usuário
     * @param id identificação única do usuário
     */

    public void deleteUser(Long id){

        User userSearched = userRepository.findById(id)
                .orElseThrow( UserNotFoundException::new);

        userSearched.setActive(false);

        userRepository.save(userSearched);
    }
}
