package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.InvalidCrBranchResponsibleRoleException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCrBranchTest {

    @Mock private CrBranchRepository crBranchRepository;
    @Mock private CrRepository crRepository;
    @Mock private BranchRepository branchRepository;
    @Mock private UserPublicApi userPublicApi;
    @Mock private CrBranchMapper crBranchMapper;

    private UpdateCrBranch updateCrBranch;
    private CrBranch crBranch;
    private Branch originalBranch;
    private Cr originalCr;

    @BeforeEach
    void setUp() {
        updateCrBranch = new UpdateCrBranch(
                crBranchRepository,
                crRepository,
                branchRepository,
                userPublicApi,
                crBranchMapper,
                new ValidateCrBranchResponsibles()
        );
        originalBranch = new Branch("Filial Centro");
        originalBranch.setId(1L);
        originalCr = new Cr("TI", "7940", false);
        originalCr.setId(2L);
        crBranch = new CrBranch(originalBranch, originalCr, new ArrayList<>(List.of(
                user(10L, Authorities.SUPERVISOR, true)
        )));
        crBranch.setId(30L);
        when(crBranchRepository.findAllByIdForUpdate(List.of(30L))).thenReturn(List.of(crBranch));
    }

    @Test
    void shouldUpdateToValidCombination() {
        Branch newBranch = new Branch("Filial Norte");
        newBranch.setId(3L);
        Cr newCr = new Cr("Compras", "7950", false);
        newCr.setId(4L);
        List<User> responsibles = List.of(
                user(11L, Authorities.SUPERVISOR, true),
                user(12L, Authorities.COORDENADOR, true)
        );
        CrBranchResponse response = new CrBranchResponse(30L, "Filial Norte", "Compras", "7950", List.of());
        arrangeDependencies(newBranch, newCr, responsibles);
        when(crBranchMapper.toResponse(crBranch)).thenReturn(response);

        CrBranchResponse result = updateCrBranch.update(30L, new CrBranchRequest(3L, 4L, List.of(11L, 12L)));

        assertThat(result).isEqualTo(response);
        assertThat(crBranch.getBranch()).isSameAs(newBranch);
        assertThat(crBranch.getCr()).isSameAs(newCr);
        assertThat(crBranch.getResponsibleUsers()).containsExactlyElementsOf(responsibles);
        verify(crBranchRepository).save(crBranch);
    }

    @Test
    void shouldPreserveStateAndNotSaveWhenCombinationIsInvalid() {
        Branch newBranch = new Branch("Filial Norte");
        newBranch.setId(3L);
        Cr newCr = new Cr("Compras", "7950", false);
        newCr.setId(4L);
        List<User> invalidResponsibles = List.of(user(11L, Authorities.DOCENTE, true));
        arrangeDependencies(newBranch, newCr, invalidResponsibles);

        assertThrows(InvalidCrBranchResponsibleRoleException.class,
                () -> updateCrBranch.update(30L, new CrBranchRequest(3L, 4L, List.of(11L))));

        assertThat(crBranch.getBranch()).isSameAs(originalBranch);
        assertThat(crBranch.getCr()).isSameAs(originalCr);
        assertThat(crBranch.getResponsibleUsers()).extracting(User::getId).containsExactly(10L);
        verify(crBranchRepository, never()).save(crBranch);
    }

    private void arrangeDependencies(Branch branch, Cr cr, List<User> responsibles) {
        when(branchRepository.findById(branch.getId())).thenReturn(Optional.of(branch));
        when(crRepository.findById(cr.getId())).thenReturn(Optional.of(cr));
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
