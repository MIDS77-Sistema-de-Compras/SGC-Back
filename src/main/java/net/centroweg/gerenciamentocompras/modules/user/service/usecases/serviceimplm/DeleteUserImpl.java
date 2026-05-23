package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserImpl {

    private final UserMapper mapper;
    private final UserRepository repository;

    public void deleteUser(Long id){
        repository.deleteById(id);
    }
}
