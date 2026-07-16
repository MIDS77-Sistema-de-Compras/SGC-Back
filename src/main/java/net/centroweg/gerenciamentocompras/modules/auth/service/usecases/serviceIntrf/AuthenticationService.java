package net.centroweg.gerenciamentocompras.modules.auth.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.auth.presentation.dto.response.ImpersonationStatusResponse;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationService {

     String login(@RequestBody LogIn loginDto);

     String impersonate(Long userId);

     String stopImpersonation(String currentToken);

     ImpersonationStatusResponse impersonationStatus(String currentToken);
}
