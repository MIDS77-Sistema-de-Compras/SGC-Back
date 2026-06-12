package net.centroweg.gerenciamentocompras.modules.auth.service.usecase.implementations;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.AuthenticationLogin;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.AuthenticationService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationLogin authenticationLogin;

    public String login(@RequestBody LogIn loginDto){
        return authenticationLogin.loginAndGenerateToken(loginDto);
    }
}
