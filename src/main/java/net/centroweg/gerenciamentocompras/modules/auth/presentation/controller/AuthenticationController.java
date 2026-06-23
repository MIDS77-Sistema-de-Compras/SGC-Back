package net.centroweg.gerenciamentocompras.modules.auth.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.AuthenticationService;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.PasswordRecoveryService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.NewPassword;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.Recovery;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

@Tag(name = "ENDPOINTS de autenticação")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final PasswordRecoveryService passwordRecoveryService;

    @Operation(description = "ENDPOINT responsável pela autenticação de usuário")
    @PostMapping("/login")
    public ResponseEntity<MessageDTO> login(@Valid @RequestBody LogIn loginDto,
                                            HttpServletResponse response){

        // gera o token
        String token = authenticationService.login(loginDto);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        // depois botar o setSecure pra true 🐌💪👉👈
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(4000);

        response.addCookie(cookie);

        return  ResponseEntity.status(200)
                .body(new MessageDTO(token));
    }

    @PostMapping("/recovery")
    public ResponseEntity<MessageDTO> sendEmailWithToken(@Valid @RequestBody Recovery recoveryDto){
        try{
            passwordRecoveryService.validateAndGenerateRecoveryToken(recoveryDto);
            return ResponseEntity.ok().body(new MessageDTO("Enviamos um email, não esqueça de conferir a caixa de spam, caso necessário."));

        }catch(MessagingException exception){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/recovery/new")
    public ResponseEntity<MessageDTO> validateAndChangePassword(@Valid @RequestBody NewPassword newPasswordDto, @RequestParam String token){
        passwordRecoveryService.changePasswordWhenValidToken(newPasswordDto, token);
        return ResponseEntity.ok().body(new MessageDTO("Senha atualizada com sucesso"));
    }

}
