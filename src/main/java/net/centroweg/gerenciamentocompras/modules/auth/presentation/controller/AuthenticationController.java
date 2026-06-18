package net.centroweg.gerenciamentocompras.modules.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.AuthenticationService;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.PasswordRecoveryService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.Recovery;
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
    private final PasswordRecoveryService passwordRecoveryService;

    @Operation(description = "ENDPOINT responsável pela autenticação de usuário")
    @PostMapping("/login")
    public ResponseEntity<MessageDTO> login(@Valid @RequestBody LogIn loginDto){


        return  ResponseEntity.status(200)
                .body(new MessageDTO(authenticationService.login(loginDto)));
    }

    @PostMapping("/password-recovery")
    public ResponseEntity<Void> sendEmailWithToken(@Valid @RequestBody Recovery recoveryDto){
        try{
            passwordRecoveryService.validateAndGenerateRecoveryToken(recoveryDto);
            return ResponseEntity.noContent().build();

        }catch(MessagingException exception){
            return ResponseEntity.internalServerError().build();
        }
    }
}
