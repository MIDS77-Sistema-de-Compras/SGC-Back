package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Caso de uso responsável por buscar um {@link User} pelo seu nome.
 */
@Service
@RequiredArgsConstructor
public class FindUserByNameImpl {

    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Lista todos os usuários cadastrados no banco de dados pelo nome informado.
     * @param name nome do usuário.
     * @return lista com todos os usuários encontrados, caso exista.
     */
    public List<UserResponse> findUserByName(String name){
        return mapper.toDTOList(repository.findByNameIgnoringCase(name));
    }
}
