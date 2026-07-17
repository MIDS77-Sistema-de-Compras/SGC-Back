package net.centroweg.gerenciamentocompras.modules.auth.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.presentation.dto.response.ImpersonationStatusResponse;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecases.AuthenticationLogin;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecases.ImpersonateUser;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecases.serviceIntrf.AuthenticationService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationLogin authenticationLogin;
    private final ImpersonateUser impersonateUser;

    public String login(@RequestBody LogIn loginDto){
        return authenticationLogin.loginAndGenerateToken(loginDto);
    }

    @Override
    public String impersonate(Long userId) {
        return impersonateUser.impersonate(userId);
    }

    @Override
    public String stopImpersonation(String currentToken) {
        return impersonateUser.stopImpersonation(currentToken);
    }

    @Override
    public ImpersonationStatusResponse impersonationStatus(String currentToken) {
        return impersonateUser.status(currentToken);
    }
}
