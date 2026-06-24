package net.centroweg.gerenciamentocompras.modules.auth.service.usecase;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;

@Service
@RequiredArgsConstructor
public class AuthenticationLogin {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String loginAndGenerateToken(@RequestBody LogIn loginDto){
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.userName(), loginDto.password());

        var authentication = authenticationManager.authenticate(authenticationToken);

        return jwtService.generateToken((UserPrincipal) authentication.getPrincipal());
    }
}
