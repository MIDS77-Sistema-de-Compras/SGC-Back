package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserByIdImpl {

    private final UserMapper mapper;
    private final UserRepository repository;

    public UserResponse findUserById(Long id){
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não foi encontrado!")));
    }
}
