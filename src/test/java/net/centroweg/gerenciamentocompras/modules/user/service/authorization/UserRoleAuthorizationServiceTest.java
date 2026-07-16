package net.centroweg.gerenciamentocompras.modules.user.service.authorization;

import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotAllowedException;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.SystemRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserRoleAuthorizationServiceTest {

    private final UserRoleAuthorizationService service =
            new UserRoleAuthorizationService(null);

    @ParameterizedTest
    @MethodSource("allowedCreateRoles")
    @DisplayName("Deve permitir criacao somente dentro da hierarquia")
    void shouldAllowCreateWhenHierarchyPermits(SystemRole actorRole, SystemRole targetRole) {
        assertDoesNotThrow(() -> service.validateCanCreate(actorRole, targetRole));
    }

    @ParameterizedTest
    @MethodSource("forbiddenCreateRoles")
    @DisplayName("Deve bloquear criacao fora da hierarquia")
    void shouldBlockCreateWhenHierarchyDoesNotPermit(SystemRole actorRole, SystemRole targetRole) {
        assertThrows(
                RoleNotAllowedException.class,
                () -> service.validateCanCreate(actorRole, targetRole)
        );
    }

    @ParameterizedTest
    @MethodSource("allowedDeactivateRoles")
    @DisplayName("Deve permitir desativacao somente dentro da hierarquia")
    void shouldAllowDeactivateWhenHierarchyPermits(SystemRole actorRole, SystemRole targetRole) {
        assertDoesNotThrow(() -> service.validateCanDeactivate(actorRole, targetRole));
    }

    @ParameterizedTest
    @MethodSource("forbiddenDeactivateRoles")
    @DisplayName("Deve bloquear desativacao fora da hierarquia")
    void shouldBlockDeactivateWhenHierarchyDoesNotPermit(SystemRole actorRole, SystemRole targetRole) {
        assertThrows(
                RoleNotAllowedException.class,
                () -> service.validateCanDeactivate(actorRole, targetRole)
        );
    }

    @ParameterizedTest
    @MethodSource("allowedDeactivateRoles")
    @DisplayName("Deve permitir alterar ativação conforme a hierarquia de desativação")
    void shouldAllowActivationChangeWhenHierarchyPermits(SystemRole actorRole, SystemRole targetRole) {
        assertDoesNotThrow(() -> service.validateCanChangeActivationStatus(actorRole, targetRole));
    }

    @ParameterizedTest
    @MethodSource("forbiddenDeactivateRoles")
    @DisplayName("Deve bloquear alteração de ativação fora da hierarquia")
    void shouldBlockActivationChangeWhenHierarchyDoesNotPermit(SystemRole actorRole, SystemRole targetRole) {
        assertThrows(
                RoleNotAllowedException.class,
                () -> service.validateCanChangeActivationStatus(actorRole, targetRole)
        );
    }

    @Test
    @DisplayName("Deve normalizar role valida")
    void shouldNormalizeValidRole() {
        assertEquals(SystemRole.COORDENADOR, SystemRole.from(" coordenador "));
    }

    @Test
    @DisplayName("Deve rejeitar role invalida ou nula")
    void shouldRejectInvalidRole() {
        assertThrows(RoleNotFoundException.class, () -> SystemRole.from("GESTOR"));
        assertThrows(RoleNotFoundException.class, () -> SystemRole.from(null));
    }

    private static Stream<Arguments> allowedCreateRoles() {
        return Stream.of(
                Arguments.of(SystemRole.SUPERVISOR, SystemRole.DOCENTE),
                Arguments.of(SystemRole.COORDENADOR, SystemRole.DOCENTE),
                Arguments.of(SystemRole.COORDENADOR, SystemRole.SUPERVISOR),
                Arguments.of(SystemRole.ADMIN, SystemRole.DOCENTE),
                Arguments.of(SystemRole.ADMIN, SystemRole.SUPERVISOR),
                Arguments.of(SystemRole.ADMIN, SystemRole.COORDENADOR),
                Arguments.of(SystemRole.ADMIN, SystemRole.COMPRADOR)
        );
    }

    private static Stream<Arguments> forbiddenCreateRoles() {
        return Stream.of(
                Arguments.of(SystemRole.SUPERVISOR, SystemRole.SUPERVISOR),
                Arguments.of(SystemRole.SUPERVISOR, SystemRole.COORDENADOR),
                Arguments.of(SystemRole.SUPERVISOR, SystemRole.COMPRADOR),
                Arguments.of(SystemRole.SUPERVISOR, SystemRole.ADMIN),
                Arguments.of(SystemRole.COORDENADOR, SystemRole.COORDENADOR),
                Arguments.of(SystemRole.COORDENADOR, SystemRole.COMPRADOR),
                Arguments.of(SystemRole.COORDENADOR, SystemRole.ADMIN),
                Arguments.of(SystemRole.ADMIN, SystemRole.ADMIN),
                Arguments.of(SystemRole.DOCENTE, SystemRole.DOCENTE),
                Arguments.of(SystemRole.DOCENTE, SystemRole.SUPERVISOR),
                Arguments.of(SystemRole.DOCENTE, SystemRole.COORDENADOR),
                Arguments.of(SystemRole.DOCENTE, SystemRole.COMPRADOR),
                Arguments.of(SystemRole.DOCENTE, SystemRole.ADMIN),
                Arguments.of(SystemRole.COMPRADOR, SystemRole.DOCENTE),
                Arguments.of(SystemRole.COMPRADOR, SystemRole.SUPERVISOR),
                Arguments.of(SystemRole.COMPRADOR, SystemRole.COORDENADOR),
                Arguments.of(SystemRole.COMPRADOR, SystemRole.COMPRADOR),
                Arguments.of(SystemRole.COMPRADOR, SystemRole.ADMIN),
                Arguments.of(null, SystemRole.DOCENTE),
                Arguments.of(SystemRole.ADMIN, null)
        );
    }

    private static Stream<Arguments> allowedDeactivateRoles() {
        return Stream.of(
                Arguments.of(SystemRole.SUPERVISOR, SystemRole.DOCENTE),
                Arguments.of(SystemRole.COORDENADOR, SystemRole.DOCENTE),
                Arguments.of(SystemRole.COORDENADOR, SystemRole.SUPERVISOR),
                Arguments.of(SystemRole.ADMIN, SystemRole.DOCENTE),
                Arguments.of(SystemRole.ADMIN, SystemRole.SUPERVISOR),
                Arguments.of(SystemRole.ADMIN, SystemRole.COORDENADOR),
                Arguments.of(SystemRole.ADMIN, SystemRole.COMPRADOR),
                Arguments.of(SystemRole.ADMIN, SystemRole.ADMIN)
        );
    }

    private static Stream<Arguments> forbiddenDeactivateRoles() {
        return Stream.of(
                Arguments.of(SystemRole.SUPERVISOR, SystemRole.SUPERVISOR),
                Arguments.of(SystemRole.SUPERVISOR, SystemRole.COORDENADOR),
                Arguments.of(SystemRole.SUPERVISOR, SystemRole.COMPRADOR),
                Arguments.of(SystemRole.SUPERVISOR, SystemRole.ADMIN),
                Arguments.of(SystemRole.COORDENADOR, SystemRole.COORDENADOR),
                Arguments.of(SystemRole.COORDENADOR, SystemRole.COMPRADOR),
                Arguments.of(SystemRole.COORDENADOR, SystemRole.ADMIN),
                Arguments.of(SystemRole.DOCENTE, SystemRole.DOCENTE),
                Arguments.of(SystemRole.COMPRADOR, SystemRole.DOCENTE),
                Arguments.of(null, SystemRole.DOCENTE),
                Arguments.of(SystemRole.ADMIN, null)
        );
    }
}
