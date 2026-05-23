package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;

@RequiredArgsConstructor
public class DeleteUserImpl {

    private final UserMapper mapper;
    private final UserRepository repository;

    public void deleteUser(Long id){
        repository.deleteById(id);
    }
}
