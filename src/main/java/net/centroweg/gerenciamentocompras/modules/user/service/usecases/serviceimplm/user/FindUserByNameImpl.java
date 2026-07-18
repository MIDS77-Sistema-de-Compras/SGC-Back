package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.List;

/**
 * Encontrar usuários pelo nome
 */

@Service
@RequiredArgsConstructor
public class FindUserByNameImpl {

    /**
     * Injeção de dependências
     */

    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Método que busca usuários pelo nome
     * @param name nome do usuário
     * @return lista de usuários que foram encontrados
     */

    public List<UserResponse> findUserByName(String name){
        return mapper.toDTOList(repository.findByNameIgnoringCase(name));
    }
}
