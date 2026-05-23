package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.UserIntrf;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserIntrf {

    private final CreateUserImpl createUser;
    private final ListUser listUser;
    private final FindUserById findUserById;

    @Override
    public UserResponse createUser(CreateUser user){
        return createUser.createUser(user);
    }

    @Override
    public List<UserResponse> listUser(){
        return listUser.listUser();
    }

    @Override
    public UserResponse findUserById(Long id){
        return findUserById.findUserById(id);
    }
}
