package net.centroweg.gerenciamentocompras.modules.auth.service.usecase;

import net.centroweg.gerenciamentocompras.modules.auth.service.usecase.implementations.AuthenticationServiceImpl;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;

    @Test
    @DisplayName("Should login successfully and return Authentication")
    void shouldLoginSuccessfully() {
        LogIn loginDto = new LogIn("maria@gmail.com", "Senha@123");

        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        Authentication result = authenticationServiceImpl.login(loginDto);

        assertNotNull(result);
        assertSame(mockAuthentication, result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Should create UsernamePasswordAuthenticationToken with correct credentials")
    void shouldCreateTokenWithCorrectCredentials() {
        LogIn loginDto = new LogIn("maria@gmail.com", "Senha@123");

        Authentication mockAuthentication = mock(Authentication.class);
        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        when(authenticationManager.authenticate(tokenCaptor.capture()))
                .thenReturn(mockAuthentication);

        authenticationServiceImpl.login(loginDto);

        UsernamePasswordAuthenticationToken capturedToken = tokenCaptor.getValue();
        assertEquals("maria@gmail.com", capturedToken.getPrincipal());
        assertEquals("Senha@123", capturedToken.getCredentials());
    }

    @Test
    @DisplayName("Should set Authentication in SecurityContextHolder after login")
    void shouldSetAuthenticationInSecurityContext() {
        LogIn loginDto = new LogIn("maria@gmail.com", "Senha@123");

        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        authenticationServiceImpl.login(loginDto);

        Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();
        assertSame(mockAuthentication, contextAuth);

        // Clean up the context after the test
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should propagate BadCredentialsException when credentials are invalid")
    void shouldPropagateBadCredentialsException() {
        LogIn loginDto = new LogIn("maria@gmail.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authenticationServiceImpl.login(loginDto)
        );
    }

    @Test
    @DisplayName("Should accept CPF as userName for login")
    void shouldAcceptCpfAsUserName() {
        LogIn loginDto = new LogIn("12345678900", "Senha@123");

        Authentication mockAuthentication = mock(Authentication.class);
        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        when(authenticationManager.authenticate(tokenCaptor.capture()))
                .thenReturn(mockAuthentication);

        Authentication result = authenticationServiceImpl.login(loginDto);

        assertNotNull(result);
        assertEquals("12345678900", tokenCaptor.getValue().getPrincipal());
    }
}
