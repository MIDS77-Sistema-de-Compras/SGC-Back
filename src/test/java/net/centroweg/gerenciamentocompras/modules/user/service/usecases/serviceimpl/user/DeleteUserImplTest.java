package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.SystemRole;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.authorization.UserRoleAuthorizationService;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.DeleteUserImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserRoleAuthorizationService authorizationService;

    @InjectMocks
    private DeleteUserImpl deleteUserImpl;

    @Test
    @DisplayName("Deve inativar (soft delete) o usuário e persistir a alteração")
    void shouldInvokeRepositoryToDeleteUser() {
        Long id = 1L;
        User user = new User();
        user.setActive(true);
        user.setRole(new Role("DOCENTE"));

        when(repository.findById(id)).thenReturn(Optional.of(user));

        // O "delete" é lógico: busca o usuário, marca como inativo e salva
        deleteUserImpl.deleteUser(id);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository, times(1)).save(captor.capture());
        verify(authorizationService).validateCanDeactivate(SystemRole.DOCENTE);
        assertFalse(captor.getValue().getActive());
    }
}
