package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.UserIntrf;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserIntrf {

    private final CreateUserImpl createUser;
    private final ListUserImpl listUser;
    private final FindUserByIdImpl findUserById;
    private final UpdateUserAllImpl updateUserAll;
    private final DeleteUserImpl deleteUser;

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

    @Override
    public UserResponse updateUserAll(Long id, CreateUser user){
        return updateUserAll.updateUserAll(id, user);
    }

    @Override
    public void deleteUser(Long id){
        deleteUser.deleteUser(id);
    }
}
