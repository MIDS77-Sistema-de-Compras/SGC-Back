package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.InvalidCrBranchResponsibleRoleException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.InvalidCrMasterResponsibleException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.MaxActiveSupervisorsException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.MaxCrBranchCoordinatorsException;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidateCrBranchResponsiblesTest {

    private final ValidateCrBranchResponsibles validator = new ValidateCrBranchResponsibles();

    @Test
    @DisplayName("Deve aceitar lista nula de responsaveis")
    void shouldAcceptNullList() {
        assertDoesNotThrow(() -> validator.validate(null));
    }

    @Test
    @DisplayName("Deve aceitar lista vazia de responsaveis")
    void shouldAcceptEmptyList() {
        assertDoesNotThrow(() -> validator.validate(List.of()));
    }

    @Test
    @DisplayName("Deve aceitar um supervisor ativo")
    void shouldAcceptOneActiveSupervisor() {
        assertDoesNotThrow(() -> validator.validate(List.of(user(Authorities.SUPERVISOR, true))));
    }

    @Test
    @DisplayName("Deve aceitar dois supervisores ativos")
    void shouldAcceptTwoActiveSupervisors() {
        assertDoesNotThrow(() -> validator.validate(List.of(
                user(Authorities.SUPERVISOR, true),
                user(Authorities.SUPERVISOR, true)
        )));
    }

    @Test
    @DisplayName("Deve rejeitar tres supervisores ativos")
    void shouldRejectThreeActiveSupervisors() {
        assertThrows(MaxActiveSupervisorsException.class, () -> validator.validate(List.of(
                user(Authorities.SUPERVISOR, true),
                user(Authorities.SUPERVISOR, true),
                user(Authorities.SUPERVISOR, true)
        )));
    }

    @Test
    @DisplayName("Supervisor inativo nao conta para o limite de ativos")
    void shouldNotCountInactiveSupervisors() {
        assertDoesNotThrow(() -> validator.validate(List.of(
                user(Authorities.SUPERVISOR, true),
                user(Authorities.SUPERVISOR, true),
                user(Authorities.SUPERVISOR, false)
        )));
    }

    @Test
    @DisplayName("Deve aceitar um coordenador")
    void shouldAcceptOneCoordinator() {
        assertDoesNotThrow(() -> validator.validate(List.of(user(Authorities.COORDENADOR, true))));
    }

    @Test
    @DisplayName("CR Master deve aceitar exatamente um coordenador ativo")
    void shouldAcceptOneActiveCoordinatorForMasterCr() {
        Cr masterCr = new Cr("CR Master", "9999", true);

        assertDoesNotThrow(() -> validator.validate(
                masterCr,
                List.of(user(Authorities.COORDENADOR, true))
        ));
    }

    @Test
    @DisplayName("CR Master não deve aceitar supervisor")
    void shouldRejectSupervisorForMasterCr() {
        Cr masterCr = new Cr("CR Master", "9999", true);

        assertThrows(InvalidCrMasterResponsibleException.class, () -> validator.validate(
                masterCr,
                List.of(user(Authorities.SUPERVISOR, true))
        ));
    }

    @Test
    @DisplayName("CR Master não deve aceitar coordenador inativo")
    void shouldRejectInactiveCoordinatorForMasterCr() {
        Cr masterCr = new Cr("CR Master", "9999", true);

        assertThrows(InvalidCrMasterResponsibleException.class, () -> validator.validate(
                masterCr,
                List.of(user(Authorities.COORDENADOR, false))
        ));
    }

    @Test
    @DisplayName("CR Master deve exigir um coordenador")
    void shouldRequireCoordinatorForMasterCr() {
        Cr masterCr = new Cr("CR Master", "9999", true);

        assertThrows(InvalidCrMasterResponsibleException.class,
                () -> validator.validate(masterCr, List.of()));
    }

    @Test
    @DisplayName("Deve rejeitar dois coordenadores")
    void shouldRejectTwoCoordinators() {
        assertThrows(MaxCrBranchCoordinatorsException.class, () -> validator.validate(List.of(
                user(Authorities.COORDENADOR, true),
                user(Authorities.COORDENADOR, false)
        )));
    }

    @Test
    @DisplayName("Deve aceitar dois supervisores ativos e um coordenador")
    void shouldAcceptTwoActiveSupervisorsAndOneCoordinator() {
        assertDoesNotThrow(() -> validator.validate(List.of(
                user(Authorities.SUPERVISOR, true),
                user(Authorities.SUPERVISOR, true),
                user(Authorities.COORDENADOR, true)
        )));
    }

    @Test
    @DisplayName("Deve rejeitar docente")
    void shouldRejectDocente() {
        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> validator.validate(List.of(user(Authorities.DOCENTE, true))));
    }

    @Test
    @DisplayName("Deve rejeitar comprador")
    void shouldRejectComprador() {
        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> validator.validate(List.of(user(Authorities.COMPRADOR, true))));
    }

    @Test
    @DisplayName("Deve rejeitar administrador")
    void shouldRejectAdmin() {
        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> validator.validate(List.of(user(Authorities.ADMIN, true))));
    }

    @Test
    @DisplayName("Deve rejeitar usuario sem role")
    void shouldRejectUserWithoutRole() {
        User user = new User();
        user.setActive(true);

        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> validator.validate(List.of(user)));
    }

    @Test
    @DisplayName("Deve rejeitar role sem nome")
    void shouldRejectRoleWithoutName() {
        User user = new User();
        user.setActive(true);
        user.setRole(new Role());

        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> validator.validate(List.of(user)));
    }

    @Test
    @DisplayName("Deve rejeitar usuario nulo")
    void shouldRejectNullUser() {
        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> validator.validate(Arrays.asList((User) null)));
    }

    @Test
    @DisplayName("Deve normalizar nomes de roles")
    void shouldNormalizeRoleNames() {
        assertDoesNotThrow(() -> validator.validate(List.of(
                user(" supervisor ", true),
                user("coordenador", true)
        )));
    }

    @Test
    @DisplayName("Deve rejeitar toda a lista ao misturar role permitida e proibida")
    void shouldRejectMixedAllowedAndForbiddenRoles() {
        assertThrows(InvalidCrBranchResponsibleRoleException.class, () -> validator.validate(List.of(
                user(Authorities.SUPERVISOR, true),
                user(Authorities.DOCENTE, true)
        )));
    }

    private User user(String roleName, boolean active) {
        User user = new User();
        user.setActive(active);
        user.setRole(new Role(roleName));
        return user;
    }
}
