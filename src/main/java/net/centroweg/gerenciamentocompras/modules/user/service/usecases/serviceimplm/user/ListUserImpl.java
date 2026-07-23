package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Caso de uso responsável por listar um {@link User}.
 */
@Service
@RequiredArgsConstructor
public class ListUserImpl {

    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Lista todos os usuários cadastrados no banco de dados.
     * @return lista com todos os usuários encontrados, caso exista.
     */
    public List<UserResponse> listUser(){
        return mapper.toDTOList(repository.findAll());
    }
}
