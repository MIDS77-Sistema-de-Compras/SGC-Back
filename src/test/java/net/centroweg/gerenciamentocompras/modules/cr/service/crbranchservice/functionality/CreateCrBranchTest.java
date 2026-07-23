package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.InvalidCrBranchResponsibleRoleException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.InvalidCrMasterResponsibleException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.MaxActiveSupervisorsException;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.MaxCrBranchCoordinatorsException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.BranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrRepository;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCrBranchTest {

    @Mock private CrBranchRepository crBranchRepository;
    @Mock private BranchRepository branchRepository;
    @Mock private CrRepository crRepository;
    @Mock private UserPublicApi userPublicApi;
    @Mock private CrBranchMapper crBranchMapper;

    private CreateCrBranch createCrBranch;
    private Branch branch;
    private Cr cr;

    @BeforeEach
    void setUp() {
        createCrBranch = new CreateCrBranch(
                crBranchRepository,
                branchRepository,
                crRepository,
                userPublicApi,
                crBranchMapper,
                new ValidateCrBranchResponsibles()
        );
        branch = new Branch("Filial Centro");
        branch.setId(1L);
        cr = new Cr("TI", "7940", false);
        cr.setId(2L);
    }

    @Test
    void shouldCreateWithTwoSupervisorsAndOneCoordinator() {
        List<User> responsibles = List.of(
                user(10L, Authorities.SUPERVISOR, true),
                user(11L, Authorities.SUPERVISOR, true),
                user(12L, Authorities.COORDENADOR, true)
        );
        CrBranch crBranch = new CrBranch(branch, cr, responsibles);
        crBranch.setId(30L);
        CrBranchResponse response = new CrBranchResponse(30L, "Filial Centro", "TI", "7940", List.of());
        arrangeCreation(responsibles);
        when(crBranchMapper.toEntity(branch, cr, responsibles)).thenReturn(crBranch);
        when(crBranchMapper.toResponse(crBranch)).thenReturn(response);

        CrBranchResponse result = createCrBranch.create(new CrBranchRequest(1L, 2L, List.of(10L, 11L, 12L)));

        assertThat(result).isEqualTo(response);
        verify(crBranchRepository).save(crBranch);
    }

    @Test
    void shouldRejectThreeActiveSupervisorsWithoutSaving() {
        List<User> responsibles = List.of(
                user(10L, Authorities.SUPERVISOR, true),
                user(11L, Authorities.SUPERVISOR, true),
                user(12L, Authorities.SUPERVISOR, true)
        );
        arrangeCreation(responsibles);

        assertThrows(MaxActiveSupervisorsException.class,
                () -> createCrBranch.create(new CrBranchRequest(1L, 2L, List.of(10L, 11L, 12L))));

        verify(crBranchRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verifyNoInteractions(crBranchMapper);
    }

    @Test
    void shouldRejectTwoCoordinatorsWithoutSaving() {
        List<User> responsibles = List.of(
                user(10L, Authorities.COORDENADOR, true),
                user(11L, Authorities.COORDENADOR, true)
        );
        arrangeCreation(responsibles);

        assertThrows(MaxCrBranchCoordinatorsException.class,
                () -> createCrBranch.create(new CrBranchRequest(1L, 2L, List.of(10L, 11L))));

        verify(crBranchRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verifyNoInteractions(crBranchMapper);
    }

    @Test
    void shouldRejectForbiddenRoleWithoutSaving() {
        List<User> responsibles = List.of(user(10L, Authorities.DOCENTE, true));
        arrangeCreation(responsibles);

        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> createCrBranch.create(new CrBranchRequest(1L, 2L, List.of(10L))));

        verify(crBranchRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verifyNoInteractions(crBranchMapper);
    }

    @Test
    void shouldRejectSupervisorForMasterCrWithoutSaving() {
        cr.setMaster(true);
        List<User> responsibles = List.of(user(10L, Authorities.SUPERVISOR, true));
        arrangeCreation(responsibles);

        assertThrows(InvalidCrMasterResponsibleException.class,
                () -> createCrBranch.create(new CrBranchRequest(1L, 2L, List.of(10L))));

        verify(crBranchRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verifyNoInteractions(crBranchMapper);
    }

    @Test
    void shouldCreateMasterCrBranchWithOneActiveCoordinator() {
        cr.setMaster(true);
        List<User> responsibles = List.of(user(10L, Authorities.COORDENADOR, true));
        CrBranch crBranch = new CrBranch(branch, cr, responsibles);
        CrBranchResponse response = new CrBranchResponse(30L, "Filial Centro", "TI", "7940", List.of("Viviane"));
        arrangeCreation(responsibles);
        when(crBranchMapper.toEntity(branch, cr, responsibles)).thenReturn(crBranch);
        when(crBranchMapper.toResponse(crBranch)).thenReturn(response);

        CrBranchResponse result = createCrBranch.create(new CrBranchRequest(1L, 2L, List.of(10L)));

        assertThat(result).isEqualTo(response);
        verify(crBranchRepository).save(crBranch);
    }

    private void arrangeCreation(List<User> responsibles) {
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(crRepository.findById(2L)).thenReturn(Optional.of(cr));
        when(crBranchRepository.findByCrIdAndBranchId(2L, 1L)).thenReturn(Optional.empty());
        when(userPublicApi.findUsersByIds(responsibles.stream().map(User::getId).toList()))
                .thenReturn(responsibles);
    }

    private User user(Long id, String roleName, boolean active) {
        User user = new User();
        user.setId(id);
        user.setActive(active);
        user.setRole(new Role(roleName));
        return user;
    }
}
