package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.List;

/**
 * Encontrar usuário pelo nome
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
     * Método que busca usuário pelo nome
     * @param name nome do usuário
     * @return usuário que foi encontrado
     */

    public List<UserResponse> findUserByName(String name){
        return mapper.toDTOList(repository.findByNameIgnoringCase(name));
    }
}
