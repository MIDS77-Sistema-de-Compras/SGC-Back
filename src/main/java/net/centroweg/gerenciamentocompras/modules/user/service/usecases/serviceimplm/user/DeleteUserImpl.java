package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserImpl {

    private final UserMapper mapper;
    private final UserRepository repository;

    public void deleteUser(Long id){
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(false);
        repository.save(user);
    }
}
