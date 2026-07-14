package net.centroweg.gerenciamentocompras.modules.cr.service.crservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.exception.CrEditNotAllowedException;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateCrMasterCoordinatorTest {

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private CrBranchRepository crBranchRepository;

    @InjectMocks
    private ValidateCrMasterCoordinator validator;

    @Test
    @DisplayName("Admin pode editar CR sem ser responsavel por CR Master")
    void shouldAllowAdmin() {
        when(currentUserService.getCurrentUser()).thenReturn(user(1L, "ADMIN"));

        assertDoesNotThrow(validator::validate);
        verifyNoInteractions(crBranchRepository);
    }

    @Test
    @DisplayName("Coordenador responsavel por CR Master pode editar")
    void shouldAllowMasterCoordinator() {
        when(currentUserService.getCurrentUser()).thenReturn(user(2L, "COORDENADOR"));
        when(crBranchRepository.existsByCrMasterTrueAndResponsibleUsersId(2L)).thenReturn(true);

        assertDoesNotThrow(validator::validate);
    }

    @Test
    @DisplayName("Coordenador sem CR Master nao pode editar")
    void shouldRejectCoordinatorWithoutMasterCr() {
        when(currentUserService.getCurrentUser()).thenReturn(user(3L, "COORDENADOR"));
        when(crBranchRepository.existsByCrMasterTrueAndResponsibleUsersId(3L)).thenReturn(false);

        assertThrows(CrEditNotAllowedException.class, validator::validate);
    }

    @Test
    @DisplayName("Outras roles nao podem editar, mesmo responsaveis por CR Master")
    void shouldRejectOtherRoles() {
        when(currentUserService.getCurrentUser()).thenReturn(user(4L, "SUPERVISOR"));

        assertThrows(CrEditNotAllowedException.class, validator::validate);
        verifyNoInteractions(crBranchRepository);
    }

    private User user(Long id, String roleName) {
        User user = new User();
        user.setId(id);
        user.setRole(new Role(roleName));
        return user;
    }
}
