package net.centroweg.gerenciamentocompras.modules.auth.service.usecases;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.auth.domain.exception.ImpersonationNotAllowedException;
import net.centroweg.gerenciamentocompras.modules.auth.presentation.dto.response.ImpersonationStatusResponse;
import net.centroweg.gerenciamentocompras.modules.auth.service.JwtService;
import net.centroweg.gerenciamentocompras.modules.auth.service.api.AuthPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImpersonateUserTest {

    @Mock private AuthPublicApi authPublicApi;
    @Mock private JwtService jwtService;
    @Mock private CurrentUserService currentUserService;

    @InjectMocks
    private ImpersonateUser impersonateUser;

    @Test
    @DisplayName("Admin deve conseguir logar na conta de um usuario ativo")
    void shouldImpersonateActiveUser() {
        User admin = user(1L, "Admin", "admin@teste.com", "ADMIN", true);
        User docente = user(2L, "Docente", "docente@teste.com", "DOCENTE", true);

        when(currentUserService.getCurrentUser()).thenReturn(admin);
        when(authPublicApi.findUserById(2L)).thenReturn(Optional.of(docente));
        when(jwtService.generateImpersonationToken(any(UserPrincipal.class), eq("admin@teste.com"), eq("Admin")))
                .thenReturn("token-impersonado");

        String token = impersonateUser.impersonate(2L);

        assertEquals("token-impersonado", token);
    }

    @Test
    @DisplayName("Nao deve permitir logar na conta de outro administrador")
    void shouldRejectImpersonatingAnotherAdmin() {
        User admin = user(1L, "Admin", "admin@teste.com", "ADMIN", true);
        User otherAdmin = user(2L, "Outro Admin", "outro@teste.com", "ADMIN", true);

        when(currentUserService.getCurrentUser()).thenReturn(admin);
        when(authPublicApi.findUserById(2L)).thenReturn(Optional.of(otherAdmin));

        assertThrows(ImpersonationNotAllowedException.class, () -> impersonateUser.impersonate(2L));
        verify(jwtService, never()).generateImpersonationToken(any(), any(), any());
    }

    @Test
    @DisplayName("Nao deve permitir logar na conta de usuario inativo")
    void shouldRejectImpersonatingInactiveUser() {
        User admin = user(1L, "Admin", "admin@teste.com", "ADMIN", true);
        User inactive = user(2L, "Inativo", "inativo@teste.com", "DOCENTE", false);

        when(currentUserService.getCurrentUser()).thenReturn(admin);
        when(authPublicApi.findUserById(2L)).thenReturn(Optional.of(inactive));

        assertThrows(ImpersonationNotAllowedException.class, () -> impersonateUser.impersonate(2L));
    }

    @Test
    @DisplayName("Nao deve permitir logar na propria conta")
    void shouldRejectImpersonatingSelf() {
        User admin = user(1L, "Admin", "admin@teste.com", "ADMIN", true);

        when(currentUserService.getCurrentUser()).thenReturn(admin);
        when(authPublicApi.findUserById(1L)).thenReturn(Optional.of(admin));

        assertThrows(ImpersonationNotAllowedException.class, () -> impersonateUser.impersonate(1L));
    }

    @Test
    @DisplayName("Deve encerrar a impersonacao e devolver token do admin original")
    void shouldStopImpersonationAndReturnAdminToken() {
        User admin = user(1L, "Admin", "admin@teste.com", "ADMIN", true);
        Claims claims = Jwts.claims();
        claims.put("impersonatedBy", "admin@teste.com");
        claims.put("impersonatedByName", "Admin");

        when(jwtService.parseClaims("token-impersonado")).thenReturn(claims);
        when(authPublicApi.findByEmailOrCpf("admin@teste.com", "admin@teste.com")).thenReturn(Optional.of(admin));
        when(jwtService.generateToken(any(UserPrincipal.class))).thenReturn("token-admin");

        String token = impersonateUser.stopImpersonation("token-impersonado");

        assertEquals("token-admin", token);
    }

    @Test
    @DisplayName("Nao deve encerrar impersonacao quando o token nao e de impersonacao")
    void shouldRejectStopWithoutImpersonationToken() {
        Claims claims = Jwts.claims();
        when(jwtService.parseClaims("token-normal")).thenReturn(claims);

        assertThrows(ImpersonationNotAllowedException.class,
                () -> impersonateUser.stopImpersonation("token-normal"));
    }

    @Test
    @DisplayName("Status deve indicar impersonacao ativa com os nomes envolvidos")
    void shouldReturnImpersonationStatus() {
        Claims claims = Jwts.claims();
        claims.put("impersonatedByName", "Admin");
        claims.put("nome", "Docente");

        when(jwtService.parseClaims("token-impersonado")).thenReturn(claims);

        ImpersonationStatusResponse status = impersonateUser.status("token-impersonado");

        assertTrue(status.impersonating());
        assertEquals("Admin", status.adminName());
        assertEquals("Docente", status.userName());
    }

    @Test
    @DisplayName("Status deve indicar sessao comum quando nao ha impersonacao")
    void shouldReturnNoImpersonationStatus() {
        ImpersonationStatusResponse status = impersonateUser.status(null);

        assertFalse(status.impersonating());
        assertNull(status.adminName());
        assertNull(status.userName());
    }

    private User user(Long id, String name, String email, String roleName, boolean active) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setActive(active);
        user.setRole(new Role(roleName));
        return user;
    }
}
