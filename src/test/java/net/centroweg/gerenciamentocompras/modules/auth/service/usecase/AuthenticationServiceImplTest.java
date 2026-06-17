package net.centroweg.gerenciamentocompras.modules.auth.service.usecase;

import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.implementations.AuthenticationServiceImpl;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationLogin authenticationLogin;

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;

    @Test
    @DisplayName("Should delegate login call to AuthenticationLogin and return token")
    void shouldDelegateLoginSuccessfully() {
        LogIn loginDto = new LogIn("maria@gmail.com", "Senha@123");
        String expectedToken = "mocked-jwt-token";
        when(authenticationLogin.loginAndGenerateToken(loginDto)).thenReturn(expectedToken);

        String resultToken = authenticationServiceImpl.login(loginDto);

        assertNotNull(resultToken);
        assertEquals(expectedToken, resultToken);
        verify(authenticationLogin, times(1)).loginAndGenerateToken(loginDto);
    }
}
