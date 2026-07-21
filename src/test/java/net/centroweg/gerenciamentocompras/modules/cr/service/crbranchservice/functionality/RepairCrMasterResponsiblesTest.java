package net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.functionality;

import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Cr;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.cr.infrastructure.persistence.repository.CrBranchRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepairCrMasterResponsiblesTest {

    @Mock private CrBranchRepository crBranchRepository;

    @Test
    void shouldRemoveSupervisorAndKeepActiveCoordinator() {
        User supervisor = user(1L, "Emersson", Authorities.SUPERVISOR, true);
        User coordinator = user(2L, "Viviane", Authorities.COORDENADOR, true);
        CrBranch masterBranch = new CrBranch();
        masterBranch.setCr(new Cr("CR Master", "9999", true));
        masterBranch.setResponsibleUsers(new ArrayList<>(List.of(supervisor, coordinator)));
        when(crBranchRepository.findAllMasterWithResponsibles()).thenReturn(List.of(masterBranch));

        RepairCrMasterResponsibles repair = new RepairCrMasterResponsibles(
                crBranchRepository,
                new ValidateCrBranchResponsibles()
        );

        repair.run(null);

        assertThat(masterBranch.getResponsibleUsers()).containsExactly(coordinator);
        verify(crBranchRepository).saveAll(List.of(masterBranch));
    }

    private User user(Long id, String name, String roleName, boolean active) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setActive(active);
        user.setRole(new Role(roleName));
        return user;
    }
}
