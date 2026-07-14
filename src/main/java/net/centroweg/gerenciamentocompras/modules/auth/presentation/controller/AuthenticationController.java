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

/**
 * Controlador REST responsável pelo gerenciamento dos endpoints de autenticação e recuperação de senha.
 */
@Tag(name = "ENDPOINTS de autenticação")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    /**
     * Serviço responsável pela autenticação dos usuários.
     */
    private final AuthenticationService authenticationService;

    /**
     * Serviço responsável pela recuperação e redefinição de senha dos usuários.
     */
    private final PasswordRecoveryService passwordRecoveryService;

    /**
     * Realiza a autenticação do usuário.
     * @param loginDto credenciais informadas pelo usuário.
     * @param response resposta HTTP utilizada para adicionar o cookie contendo o JWT.
     * @return resposta contendo o token gerado.
     */
    @Operation(description = "ENDPOINT responsável pela autenticação de usuário")
    @PostMapping("/login")
    public ResponseEntity<MessageDTO> login(@Valid @RequestBody LogIn loginDto,
                                            HttpServletResponse response){

        String token = authenticationService.login(loginDto);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(4000);

        response.addCookie(cookie);

        return  ResponseEntity.status(200)
                .body(new MessageDTO(token));
    }

    /**
     * Inicia o processo de recuperação de senha.
     * @param recoveryDto dados necessários para localizar o usuário.
     * @return mensagem informando o envio de e-mail de recuperação.
     */
    @PostMapping("/recovery")
    public ResponseEntity<MessageDTO> sendEmailWithToken(@Valid @RequestBody Recovery recoveryDto){
        try{
            passwordRecoveryService.validateAndGenerateRecoveryToken(recoveryDto);
            return ResponseEntity.ok().body(new MessageDTO("Enviamos um e-mail, não esqueça de conferir a caixa de spam, caso necessário."));

        }catch(MessagingException exception){
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Altera a senha do usuário utilizando um token de recuperação.
     * @param newPasswordDto objeto contendo a nova senha.
     * @param token token de recuperação enviado por e-mail.
     * @return mensagem indicando que a senha foi alterada.
     */
    @PostMapping("/recovery/new")
    public ResponseEntity<MessageDTO> validateAndChangePassword(@Valid @RequestBody NewPassword newPasswordDto, @RequestParam String token){
        passwordRecoveryService.changePasswordWhenValidToken(newPasswordDto, token);
        return ResponseEntity.ok().body(new MessageDTO("Senha atualizada com sucesso!"));
    }

}
