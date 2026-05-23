package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;

import java.util.List;

public interface UserIntrf {

    UserResponse createUser(CreateUser user);
    List<UserResponse> listUser();
    UserResponse findUserById(Long id);
    List<UserResponse> findUserByName(String name);
    UserResponse updateUserAll(Long id, CreateUser user);
    void deleteUser(Long id);
}
