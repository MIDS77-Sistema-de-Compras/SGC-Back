package net.centroweg.gerenciamentocompras.modules.auth.service.usecase;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por realizar a autenticação do usuário e gerar um token JWT para acesso aos recursos protegidos da aplicação.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationLogin {

    /**
     * Responsável por realizar a autenticação das credenciais do usuário.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Serviço responsável pela geração de tokens JWT.
     */
    private final JwtService jwtService;

    /**
     * Autentica o usuário e gera um token JWT.
     * @param loginDto objeto contendo o nome do usuário e a senha.
     * @return token JWT gerado após a autenticação bem-sucedida.
     */
    public String loginAndGenerateToken(LogIn loginDto){
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.userName(), loginDto.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        return jwtService.generateToken((UserPrincipal) authentication.getPrincipal());
    }
}
