package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.InvalidCrBranchResponsibleRoleException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.InvalidCrMasterResponsibleException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.MaxActiveSupervisorsException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.MaxCrBranchCoordinatorsException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.service.mapper.CrBranchMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignCrBranchResponsibleTest {

    @Mock private CrBranchRepository crBranchRepository;
    @Mock private UserPublicApi userPublicApi;
    @Mock private CrBranchMapper crBranchMapper;

    private AssignCrBranchResponsible assignCrBranchResponsible;
    private CrBranch crBranch;

    @BeforeEach
    void setUp() {
        assignCrBranchResponsible = new AssignCrBranchResponsible(
                crBranchRepository,
                userPublicApi,
                crBranchMapper,
                new ValidateCrBranchResponsibles()
        );
        crBranch = new CrBranch();
        crBranch.setId(30L);
        crBranch.setResponsibleUsers(new ArrayList<>());
        when(crBranchRepository.findAllByIdForUpdate(List.of(30L))).thenReturn(List.of(crBranch));
    }

    @Test
    void shouldAddFirstSupervisor() {
        User supervisor = user(10L, Authorities.SUPERVISOR, true);
        when(userPublicApi.findUserById(10L)).thenReturn(Optional.of(supervisor));

        assignCrBranchResponsible.assignCrBranchResponsible(30L, 10L);

        assertThat(crBranch.getResponsibleUsers()).containsExactly(supervisor);
        verify(crBranchRepository).findAllByIdForUpdate(List.of(30L));
        verify(crBranchRepository).save(crBranch);
    }

    @Test
    void shouldAddSecondSupervisor() {
        crBranch.setResponsibleUsers(new ArrayList<>(List.of(user(10L, Authorities.SUPERVISOR, true))));
        User secondSupervisor = user(11L, Authorities.SUPERVISOR, true);
        when(userPublicApi.findUserById(11L)).thenReturn(Optional.of(secondSupervisor));

        assignCrBranchResponsible.assignCrBranchResponsible(30L, 11L);

        assertThat(crBranch.getResponsibleUsers()).extracting(User::getId).containsExactly(10L, 11L);
        verify(crBranchRepository).save(crBranch);
    }

    @Test
    void shouldRejectThirdActiveSupervisorWithoutChangingCollection() {
        crBranch.setResponsibleUsers(new ArrayList<>(List.of(
                user(10L, Authorities.SUPERVISOR, true),
                user(11L, Authorities.SUPERVISOR, true)
        )));
        User thirdSupervisor = user(12L, Authorities.SUPERVISOR, true);
        when(userPublicApi.findUserById(12L)).thenReturn(Optional.of(thirdSupervisor));

        assertThrows(MaxActiveSupervisorsException.class,
                () -> assignCrBranchResponsible.assignCrBranchResponsible(30L, 12L));

        assertThat(crBranch.getResponsibleUsers()).extracting(User::getId).containsExactly(10L, 11L);
        verify(crBranchRepository, never()).save(crBranch);
    }

    @Test
    void shouldAddCoordinator() {
        User coordinator = user(10L, Authorities.COORDENADOR, true);
        when(userPublicApi.findUserById(10L)).thenReturn(Optional.of(coordinator));

        assignCrBranchResponsible.assignCrBranchResponsible(30L, 10L);

        assertThat(crBranch.getResponsibleUsers()).containsExactly(coordinator);
        verify(crBranchRepository).save(crBranch);
    }

    @Test
    void shouldRejectSecondCoordinatorWithoutChangingCollection() {
        crBranch.setResponsibleUsers(new ArrayList<>(List.of(user(10L, Authorities.COORDENADOR, true))));
        User secondCoordinator = user(11L, Authorities.COORDENADOR, true);
        when(userPublicApi.findUserById(11L)).thenReturn(Optional.of(secondCoordinator));

        assertThrows(MaxCrBranchCoordinatorsException.class,
                () -> assignCrBranchResponsible.assignCrBranchResponsible(30L, 11L));

        assertThat(crBranch.getResponsibleUsers()).extracting(User::getId).containsExactly(10L);
        verify(crBranchRepository, never()).save(crBranch);
    }

    @Test
    void shouldRejectDocenteWithoutSaving() {
        User docente = user(10L, Authorities.DOCENTE, true);
        when(userPublicApi.findUserById(10L)).thenReturn(Optional.of(docente));

        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> assignCrBranchResponsible.assignCrBranchResponsible(30L, 10L));

        assertThat(crBranch.getResponsibleUsers()).isEmpty();
        verify(crBranchRepository, never()).save(crBranch);
    }

    @Test
    void shouldRejectCompradorWithoutSaving() {
        User comprador = user(10L, Authorities.COMPRADOR, true);
        when(userPublicApi.findUserById(10L)).thenReturn(Optional.of(comprador));

        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> assignCrBranchResponsible.assignCrBranchResponsible(30L, 10L));

        assertThat(crBranch.getResponsibleUsers()).isEmpty();
        verify(crBranchRepository, never()).save(crBranch);
    }

    @Test
    void shouldRejectAdminWithoutSaving() {
        User admin = user(10L, Authorities.ADMIN, true);
        when(userPublicApi.findUserById(10L)).thenReturn(Optional.of(admin));

        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> assignCrBranchResponsible.assignCrBranchResponsible(30L, 10L));

        assertThat(crBranch.getResponsibleUsers()).isEmpty();
        verify(crBranchRepository, never()).save(crBranch);
    }

    @Test
    void shouldRemainIdempotentWhenUserIsAlreadyResponsible() {
        User supervisor = user(10L, Authorities.SUPERVISOR, true);
        crBranch.setResponsibleUsers(new ArrayList<>(List.of(supervisor)));
        when(userPublicApi.findUserById(10L)).thenReturn(Optional.of(supervisor));

        assignCrBranchResponsible.assignCrBranchResponsible(30L, 10L);

        assertThat(crBranch.getResponsibleUsers()).containsExactly(supervisor);
        verify(crBranchRepository, never()).save(crBranch);
    }

    @Test
    void shouldRejectSupervisorForMasterCr() {
        crBranch.setCr(new Cr("CR Master", "9999", true));
        User supervisor = user(10L, Authorities.SUPERVISOR, true);
        when(userPublicApi.findUserById(10L)).thenReturn(Optional.of(supervisor));

        assertThrows(InvalidCrMasterResponsibleException.class,
                () -> assignCrBranchResponsible.assignCrBranchResponsible(30L, 10L));

        assertThat(crBranch.getResponsibleUsers()).isEmpty();
        verify(crBranchRepository, never()).save(crBranch);
    }

    private User user(Long id, String roleName, boolean active) {
        User user = new User();
        user.setId(id);
        user.setActive(active);
        user.setRole(new Role(roleName));
        return user;
    }
}
