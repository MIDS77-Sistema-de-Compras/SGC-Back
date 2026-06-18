package net.centroweg.gerenciamentocompras.modules.auth.service.usecase;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.LogIn;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationLoginTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationLogin authenticationLogin;

    private UserPrincipal userPrincipal;
    private LogIn loginDto;

    @BeforeEach
    void setUp() {
        Role role = new Role("ADMIN");
        role.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setName("Maria Eduarda");
        user.setEmail("maria@gmail.com");
        user.setCpf("12345678900");
        user.setPassword("encryptedPassword");
        user.setExtensionNumber("1234");
        user.setActive(true);
        user.setRole(role);

        userPrincipal = new UserPrincipal(user);
        loginDto = new LogIn("maria@gmail.com", "Senha@123");
    }

    @Test
    @DisplayName("Should login successfully and return JWT token")
    void shouldLoginAndGenerateTokenSuccessfully() {
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(userPrincipal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken(userPrincipal)).thenReturn("mocked-jwt-token");

        String token = authenticationLogin.loginAndGenerateToken(loginDto);

        assertNotNull(token);
        assertEquals("mocked-jwt-token", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userPrincipal);
    }

    @Test
    @DisplayName("Should create UsernamePasswordAuthenticationToken with correct credentials")
    void shouldCreateTokenWithCorrectCredentials() {
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(userPrincipal);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        when(authenticationManager.authenticate(tokenCaptor.capture()))
                .thenReturn(mockAuthentication);
        when(jwtService.generateToken(userPrincipal)).thenReturn("mocked-jwt-token");

        authenticationLogin.loginAndGenerateToken(loginDto);

        UsernamePasswordAuthenticationToken capturedToken = tokenCaptor.getValue();
        assertEquals("maria@gmail.com", capturedToken.getPrincipal());
        assertEquals("Senha@123", capturedToken.getCredentials());
    }

    @Test
    @DisplayName("Should propagate BadCredentialsException when credentials are invalid")
    void shouldPropagateBadCredentialsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authenticationLogin.loginAndGenerateToken(loginDto)
        );
        verifyNoInteractions(jwtService);
    }
}
