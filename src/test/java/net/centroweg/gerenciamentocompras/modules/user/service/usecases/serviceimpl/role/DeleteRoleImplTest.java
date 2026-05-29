package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.role;

import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role.DeleteRoleImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRoleImplTest {

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private DeleteRoleImpl deleteRoleImpl;

    @Test
    @DisplayName("Deve chamar deleteById com o ID correto")
    void shouldCallDeleteByIdWithCorrectId() {
        Long id = 1L;

        deleteRoleImpl.deleteRole(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Não deve chamar deleteById mais de uma vez")
    void shouldCallDeleteByIdOnlyOnce() {
        Long id = 5L;

        deleteRoleImpl.deleteRole(id);

        verify(repository, times(1)).deleteById(id);
        verifyNoMoreInteractions(repository);
    }
}