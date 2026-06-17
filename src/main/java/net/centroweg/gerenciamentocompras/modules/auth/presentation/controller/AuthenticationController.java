package net.centroweg.gerenciamentocompras.modules.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.AuthenticationService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ENDPOINTS de autenticação")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(description = "ENDPOINT responsável pela autenticação de usuário")
    @PostMapping("/login")
    public ResponseEntity<MessageDTO> login(@Valid @RequestBody LogIn loginDto,
                                            HttpServletResponse response){

        // gera o token
        String token = authenticationService.login(loginDto);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        // depois botar o setSecure pra true
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(4000);

        response.addCookie(cookie);

        return  ResponseEntity.status(200)
                .body(new MessageDTO("login realizado com sucesso"));
    }
}
