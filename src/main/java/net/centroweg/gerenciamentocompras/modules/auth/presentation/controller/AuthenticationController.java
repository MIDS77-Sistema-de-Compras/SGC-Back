package net.centroweg.gerenciamentocompras.modules.auth.presentation.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.presentation.dto.response.ImpersonationStatusResponse;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecases.serviceIntrf.AuthenticationService;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecases.serviceIntrf.PasswordRecoveryService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.NewPassword;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.Recovery;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import net.centroweg.gerenciamentocompras.shared.annotation.RateLimit;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.AuditParam;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.Auditable;
import net.centroweg.gerenciamentocompras.shared.security.annotation.AdminOnly;

@Tag(name = "ENDPOINTS de autenticação")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final PasswordRecoveryService passwordRecoveryService;

    @Value("${app.cookies.secure}")
    private boolean secureCookie;

    @Operation(description = "ENDPOINT responsável pela autenticação de usuário")
    @PostMapping("/login")
    @RateLimit(profile = "login")
    @Auditable(action = "LOGAR")
    public ResponseEntity<MessageDTO> login(@Valid @RequestBody LogIn loginDto,
                                            HttpServletResponse response){

        String token = authenticationService.login(loginDto);

        addJwtCookie(response, token);

        return ResponseEntity.status(200)
                .body(new MessageDTO(token));

    }

    /**
     * Permite que um administrador entre na conta de outro usuário,
     * assumindo as permissões dele. As ações executadas durante a
     * impersonação são auditadas com a identificação do administrador.
     */
    @Operation(description = "ENDPOINT responsável por logar o administrador na conta de outro usuário")
    @PostMapping("/impersonate/{userId}")
    @AdminOnly
    @Auditable(action = "LOGAR_COMO_USUARIO")
    public ResponseEntity<MessageDTO> impersonate(@AuditParam("user") @PathVariable Long userId,
                                                  HttpServletResponse response){

        String token = authenticationService.impersonate(userId);

        addJwtCookie(response, token);

        return ResponseEntity.status(200)
                .body(new MessageDTO(token));
    }

    /**
     * Encerra a impersonação e devolve a sessão do administrador original.
     */
    @Operation(description = "ENDPOINT responsável por encerrar a impersonação e voltar à conta do administrador")
    @PostMapping("/impersonate/stop")
    @Auditable(action = "VOLTAR_PARA_CONTA_ADMIN")
    public ResponseEntity<MessageDTO> stopImpersonation(HttpServletRequest request,
                                                        HttpServletResponse response){

        String newToken = authenticationService.stopImpersonation(extractJwt(request));

        addJwtCookie(response, newToken);

        return ResponseEntity.status(200)
                .body(new MessageDTO(newToken));
    }

    /**
     * Informa se a sessão atual é de impersonação (usado pelo frontend
     * para exibir o aviso de "logado como outro usuário").
     */
    @Operation(description = "ENDPOINT responsável por informar o estado de impersonação da sessão")
    @GetMapping("/impersonate/status")
    public ResponseEntity<ImpersonationStatusResponse> impersonationStatus(HttpServletRequest request){
        return ResponseEntity.ok(authenticationService.impersonationStatus(extractJwt(request)));
    }

    private void addJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookie);
        cookie.setPath("/");
        cookie.setMaxAge(4000);
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);
    }

    private String extractJwt(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    @PostMapping("/recovery")
    @RateLimit(profile = "recovery")
    @Auditable(action = "SOLICITAR_ALTERACAO_SENHA")
    public ResponseEntity<MessageDTO> sendEmailWithToken(@Valid @RequestBody Recovery recoveryDto){
        try{
            passwordRecoveryService.validateAndGenerateRecoveryToken(recoveryDto);
            return ResponseEntity.ok().body(new MessageDTO("Enviamos um email, não esqueça de conferir a caixa de spam, caso necessário."));

        }catch(MessagingException exception){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/recovery/new")
    @Auditable(action = "ALTERAR_SENHA")
    public ResponseEntity<MessageDTO> validateAndChangePassword(@Valid @RequestBody NewPassword newPasswordDto, @RequestParam String token){
        passwordRecoveryService.changePasswordWhenValidToken(newPasswordDto, token);
        return ResponseEntity.ok().body(new MessageDTO("Senha atualizada com sucesso"));
    }

}
