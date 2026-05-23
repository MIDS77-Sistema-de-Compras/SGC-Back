package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;

import java.util.List;

public interface UserIntrf {

    UserResponse createUser(CreateUser user);
    List<UserResponse> listUser();
    UserResponse findUserById(Long id);
    UserResponse updateUserAll(Long id, CreateUser user);
}
