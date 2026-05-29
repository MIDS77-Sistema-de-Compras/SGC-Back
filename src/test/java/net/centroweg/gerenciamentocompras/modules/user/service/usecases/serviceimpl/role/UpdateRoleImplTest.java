package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.role;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role.UpdateRoleImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRoleImplTest {

    @Mock
    private RoleMapper mapper;

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private UpdateRoleImpl updateRoleImpl;

    @Test
    @DisplayName("Deve atualizar o nome da role corretamente")
    void shouldUpdateRoleNameCorrectly() {
        Long id = 1L;
        CreateRole request = new CreateRole("SUPER_ADMIN");
        Role existingRole = new Role();
        existingRole.setName("ADMIN");
        RoleResponse expectedResponse = new RoleResponse(id, "SUPER_ADMIN");

        when(repository.findById(id)).thenReturn(Optional.of(existingRole));
        when(repository.save(any(Role.class))).thenAnswer(i -> i.getArguments()[0]);
        when(mapper.toDTO(any(Role.class))).thenReturn(expectedResponse);

        RoleResponse result = updateRoleImpl.updateRole(id, request);

        assertNotNull(result);
        assertEquals("SUPER_ADMIN", result.name());

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
        verify(repository).save(captor.capture());
        assertEquals("SUPER_ADMIN", captor.getValue().getName());
    }

    @Test
    @DisplayName("Deve lançar RoleNotFoundException quando a role não existir")
    void shouldThrowExceptionWhenRoleNotFound() {
        Long id = 99L;
        CreateRole request = new CreateRole("QUALQUER");

        when(repository.findById(id)).thenReturn(Optional.empty());

        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class, () ->
                updateRoleImpl.updateRole(id, request)
        );

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }
}