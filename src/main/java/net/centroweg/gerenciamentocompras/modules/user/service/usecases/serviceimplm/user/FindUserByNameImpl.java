package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindUserByNameImpl {

    private final UserMapper mapper;
    private final UserRepository repository;

    public List<UserResponse> findUserByName(String name){
        return mapper.toDTOList(repository.findByNameIgnoringCase(name));
    }
}
