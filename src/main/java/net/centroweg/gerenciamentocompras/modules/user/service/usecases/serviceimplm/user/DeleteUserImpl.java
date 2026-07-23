package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Caso de uso responsável por remover um {@link User}.
 */
@Service
@RequiredArgsConstructor
public class DeleteUserImpl {

    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Remove um usuário do banco de dados.
     * @param id identificador do usuário.
     */
    public void deleteUser(Long id){
        repository.deleteById(id);
    }
}
