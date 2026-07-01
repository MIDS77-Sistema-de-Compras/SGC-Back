package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Listar usuários
 */

@Service
@RequiredArgsConstructor
public class ListUserImpl {

    /**
     * Injeção de dependências
     */

    private final UserMapper mapper;
    private final UserRepository repository;

    /**
     * Método que lista todos usuário
     * @return lista de usuários
     */

    public Page<UserResponse> listUser(Pageable pageable){
        return repository.findAll(pageable).map(mapper::toDTO);
    }
}
