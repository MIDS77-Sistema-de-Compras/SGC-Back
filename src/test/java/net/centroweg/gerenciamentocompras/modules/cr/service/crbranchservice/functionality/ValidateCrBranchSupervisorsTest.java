package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.MaxActiveSupervisorsException;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateCrBranchSupervisorsTest {

    private final ValidateCrBranchSupervisors validator = new ValidateCrBranchSupervisors();

    @Test
    @DisplayName("Deve aceitar lista nula de responsaveis")
    void shouldAcceptNullList() {
        assertDoesNotThrow(() -> validator.validate(null));
    }

    @Test
    @DisplayName("Deve aceitar ate dois supervisores ativos")
    void shouldAcceptUpToTwoActiveSupervisors() {
        List<User> responsibles = List.of(
                user("SUPERVISOR", true),
                user("SUPERVISOR", true)
        );

        assertDoesNotThrow(() -> validator.validate(responsibles));
    }

    @Test
    @DisplayName("Deve rejeitar tres supervisores ativos")
    void shouldRejectThreeActiveSupervisors() {
        List<User> responsibles = List.of(
                user("SUPERVISOR", true),
                user("SUPERVISOR", true),
                user("SUPERVISOR", true)
        );

        assertThrows(MaxActiveSupervisorsException.class, () -> validator.validate(responsibles));
    }

    @Test
    @DisplayName("Supervisor inativo nao conta para o limite")
    void shouldNotCountInactiveSupervisors() {
        List<User> responsibles = List.of(
                user("SUPERVISOR", true),
                user("SUPERVISOR", true),
                user("SUPERVISOR", false)
        );

        assertDoesNotThrow(() -> validator.validate(responsibles));
    }

    @Test
    @DisplayName("Usuarios de outras roles nao contam para o limite")
    void shouldNotCountOtherRoles() {
        List<User> responsibles = List.of(
                user("SUPERVISOR", true),
                user("SUPERVISOR", true),
                user("DOCENTE", true),
                user("COORDENADOR", true)
        );

        assertDoesNotThrow(() -> validator.validate(responsibles));
    }

    private User user(String roleName, boolean active) {
        User user = new User();
        user.setActive(active);
        user.setRole(new Role(roleName));
        return user;
    }
}
