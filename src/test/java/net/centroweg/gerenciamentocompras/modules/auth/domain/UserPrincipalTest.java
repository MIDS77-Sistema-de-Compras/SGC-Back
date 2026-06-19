package net.centroweg.gerenciamentocompras.modules.auth.domain;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserPrincipalTest {

    private User user;
    private UserPrincipal userPrincipal;

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

        userPrincipal = new UserPrincipal(user);
    }

    @Test
    @DisplayName("Should return authorities based on user role")
    void shouldReturnAuthoritiesCorrectly() {
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("Should return user password")
    void shouldReturnPassword() {
        assertEquals("encryptedPassword", userPrincipal.getPassword());
    }

    @Test
    @DisplayName("Should return email as username")
    void shouldReturnEmailAsUsername() {
        assertEquals("maria@gmail.com", userPrincipal.getUsername());
    }

    @Test
    @DisplayName("Should return true for isAccountNonExpired")
    void shouldReturnAccountNonExpired() {
        assertTrue(userPrincipal.isAccountNonExpired());
    }

    @Test
    @DisplayName("Should return true for isAccountNonLocked")
    void shouldReturnAccountNonLocked() {
        assertTrue(userPrincipal.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should return true for isCredentialsNonExpired")
    void shouldReturnCredentialsNonExpired() {
        assertTrue(userPrincipal.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Should return true for isEnabled")
    void shouldReturnEnabled() {
        assertTrue(userPrincipal.isEnabled());
    }

    @Test
    @DisplayName("Should return null when user password is null")
    void shouldReturnNullWhenPasswordIsNull() {
        user.setPassword(null);
        assertNull(userPrincipal.getPassword());
    }
}
