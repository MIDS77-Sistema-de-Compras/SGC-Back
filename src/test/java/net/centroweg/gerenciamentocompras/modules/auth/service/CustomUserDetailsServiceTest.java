package net.centroweg.gerenciamentocompras.modules.auth.service;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.service.api.AuthPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private AuthPublicApi authPublicApi;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        Role role = new Role("ADMIN");
        role.setId(1L);

        user = new User();
        user.setId(1L);
        user.setName("Maria Eduarda");
        user.setEmail("maria@gmail.com");
        user.setCpf("12345678900");
        user.setPassword("encryptedPassword");
        user.setExtensionNumber("1234");
        user.setActive(true);
        user.setRole(role);
    }

    @Test
    @DisplayName("Should load user by email successfully")
    void shouldLoadUserByEmailSuccessfully() {
        when(authPublicApi.findByEmailOrCpf("maria@gmail.com", "mariagmailcom"))
                .thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("maria@gmail.com");

        assertNotNull(result);
        assertInstanceOf(UserPrincipal.class, result);
        assertEquals("maria@gmail.com", result.getUsername());
        assertEquals("encryptedPassword", result.getPassword());
        verify(authPublicApi).findByEmailOrCpf("maria@gmail.com", "mariagmailcom");
    }

    @Test
    @DisplayName("Should load user by CPF successfully")
    void shouldLoadUserByCpfSuccessfully() {
        when(authPublicApi.findByEmailOrCpf("12345678900", "12345678900"))
                .thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("12345678900");

        assertNotNull(result);
        assertInstanceOf(UserPrincipal.class, result);
        assertEquals("maria@gmail.com", result.getUsername());
        verify(authPublicApi).findByEmailOrCpf("12345678900", "12345678900");
    }

    @Test
    @DisplayName("Should load user by formatted CPF successfully")
    void shouldLoadUserByFormattedCpfSuccessfully() {
        when(authPublicApi.findByEmailOrCpf("123.456.789-00", "12345678900"))
                .thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("123.456.789-00");

        assertNotNull(result);
        verify(authPublicApi).findByEmailOrCpf("123.456.789-00", "12345678900");
    }

    @Test
    @DisplayName("Should trim login input before searching")
    void shouldTrimLoginBeforeSearching() {
        when(authPublicApi.findByEmailOrCpf("maria@gmail.com", "mariagmailcom"))
                .thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("  maria@gmail.com  ");

        assertNotNull(result);
        verify(authPublicApi).findByEmailOrCpf("maria@gmail.com", "mariagmailcom");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user is not found")
    void shouldThrowExceptionWhenUserNotFound() {
        when(authPublicApi.findByEmailOrCpf(anyString(), anyString()))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("nonexistent@gmail.com")
        );

        assertEquals("Credenciais inválidas para o login fornecido", exception.getMessage());
    }
}
