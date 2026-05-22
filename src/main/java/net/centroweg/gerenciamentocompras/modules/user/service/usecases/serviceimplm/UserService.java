package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.UserIntrf;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserIntrf {

    private final CreateUserImpl createUser;

    @Override
    public UserResponse createUser(CreateUser user){
        return createUser.createUser(user);
    }
}
