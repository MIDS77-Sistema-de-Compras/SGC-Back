package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
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

    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Método que deleta usuário
     * @param id identificação única do usuário
     */

    public void deleteUser(Long id){
        repository.deleteById(id);
    }
}
