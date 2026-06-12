package net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces;

import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationService {

     String login(@RequestBody LogIn loginDto);
}
