package net.centroweg.gerenciamentocompras.modules.auth.service.usecase.implementations;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.AuthenticationLogin;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.AuthenticationService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço de autenticação da aplicação.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * Responsável por autenticar o usuário e gerar o token JWT.
     */
    private final AuthenticationLogin authenticationLogin;

    /**
     * Realiza a autenticação do usuário.
     * @param loginDto objeto contendo as credencias de acesso do usuário.
     * @return token JWT válido gerado após a autenticação bem-sucedida.
     */
    public String login(LogIn loginDto){
        return authenticationLogin.loginAndGenerateToken(loginDto);
    }
}
