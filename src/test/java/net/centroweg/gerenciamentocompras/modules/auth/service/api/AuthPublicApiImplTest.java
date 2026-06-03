package net.centroweg.gerenciamentocompras.modules.auth.service.api;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthPublicApiImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthPublicApiImpl authPublicApiImpl;

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
    @DisplayName("Should find user by email successfully")
    void shouldFindUserByEmail() {
        when(userRepository.findByEmailOrCpf("maria@gmail.com", "12345678900"))
                .thenReturn(Optional.of(user));

        Optional<User> result = authPublicApiImpl.findByEmailOrCpf("maria@gmail.com", "12345678900");

        assertTrue(result.isPresent());
        assertEquals("Maria Eduarda", result.get().getName());
        assertEquals("maria@gmail.com", result.get().getEmail());
        verify(userRepository).findByEmailOrCpf("maria@gmail.com", "12345678900");
    }

    @Test
    @DisplayName("Should find user by CPF successfully")
    void shouldFindUserByCpf() {
        when(userRepository.findByEmailOrCpf("other@email.com", "12345678900"))
                .thenReturn(Optional.of(user));

        Optional<User> result = authPublicApiImpl.findByEmailOrCpf("other@email.com", "12345678900");

        assertTrue(result.isPresent());
        assertEquals("12345678900", result.get().getCpf());
        verify(userRepository).findByEmailOrCpf("other@email.com", "12345678900");
    }

    @Test
    @DisplayName("Should return empty Optional when user is not found")
    void shouldReturnEmptyOptionalWhenNotFound() {
        when(userRepository.findByEmailOrCpf("nonexistent@gmail.com", "00000000000"))
                .thenReturn(Optional.empty());

        Optional<User> result = authPublicApiImpl.findByEmailOrCpf("nonexistent@gmail.com", "00000000000");

        assertFalse(result.isPresent());
        verify(userRepository).findByEmailOrCpf("nonexistent@gmail.com", "00000000000");
    }

    @Test
    @DisplayName("Should delegate the call correctly to UserRepository")
    void shouldDelegateToUserRepository() {
        when(userRepository.findByEmailOrCpf(anyString(), anyString()))
                .thenReturn(Optional.of(user));

        authPublicApiImpl.findByEmailOrCpf("test@email.com", "99999999999");

        verify(userRepository, times(1)).findByEmailOrCpf("test@email.com", "99999999999");
        verifyNoMoreInteractions(userRepository);
    }
}
