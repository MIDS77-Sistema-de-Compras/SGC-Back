package net.centroweg.gerenciamentocompras.modules.auth.presentation.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.AuthenticationService;
import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.interfaces.PasswordRecoveryService;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.NewPassword;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.Recovery;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Testes unitários (Mockito, sem contexto Spring) do {@link AuthenticationController}.
 *
 * <p>Focam no contrato de negócio do controller: delegação correta aos serviços e status HTTP.
 * A presença das anotações {@code @Auditable} é validada separadamente em
 * {@code net.centroweg.gerenciamentocompras.shared.audit.AuditAnnotationsTest}.</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationController - testes unitários")
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PasswordRecoveryService passwordRecoveryService;

    @Mock
    private HttpServletResponse httpResponse;

    @InjectMocks
    private AuthenticationController controller;

    @Test
    @DisplayName("POST /login deve autenticar, gravar cookie jwt e retornar 200 com o token")
    void login_deveRetornarTokenEGravarCookie() {
        LogIn loginDto = new LogIn("maria@gmail.com", "Senha@123");
        when(authenticationService.login(loginDto)).thenReturn("jwt-token-123");

        ResponseEntity<MessageDTO> response = controller.login(loginDto, httpResponse);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().text()).isEqualTo("jwt-token-123");

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(httpResponse).addCookie(cookieCaptor.capture());
        Cookie cookie = cookieCaptor.getValue();
        assertThat(cookie.getName()).isEqualTo("jwt");
        assertThat(cookie.getValue()).isEqualTo("jwt-token-123");
        assertThat(cookie.isHttpOnly()).isTrue();

        verify(authenticationService, times(1)).login(loginDto);
    }

    @Test
    @DisplayName("POST /recovery deve delegar ao serviço de recuperação e retornar 200")
    void recovery_deveDelegarERetornarOk() throws MessagingException {
        Recovery recovery = new Recovery("maria@gmail.com");

        ResponseEntity<MessageDTO> response = controller.sendEmailWithToken(recovery);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(passwordRecoveryService, times(1)).validateAndGenerateRecoveryToken(recovery);
    }

    @Test
    @DisplayName("POST /recovery deve retornar 500 quando o envio de e-mail falha")
    void recovery_deveRetornar500QuandoMessagingException() throws MessagingException {
        Recovery recovery = new Recovery("maria@gmail.com");
        doThrow(new MessagingException("falha smtp"))
                .when(passwordRecoveryService).validateAndGenerateRecoveryToken(recovery);

        ResponseEntity<MessageDTO> response = controller.sendEmailWithToken(recovery);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("POST /recovery/new deve validar token, alterar a senha e retornar 200")
    void changePassword_deveDelegarERetornarOk() {
        NewPassword newPassword = new NewPassword("NovaSenha@123");
        String token = "reset-token";

        ResponseEntity<MessageDTO> response = controller.validateAndChangePassword(newPassword, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(passwordRecoveryService, times(1)).changePasswordWhenValidToken(newPassword, token);
    }
}
