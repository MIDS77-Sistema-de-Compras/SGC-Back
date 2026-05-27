package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.role;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role.FindRoleByIdImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindRoleByIdImplTest {

    @Mock
    private RoleMapper mapper;

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private FindRoleByIdImpl findRoleByIdImpl;

    @Test
    @DisplayName("Deve retornar a role quando o ID existir")
    void shouldReturnRoleWhenIdExists() {
        Long id = 1L;
        Role role = new Role();
        role.setName("ADMIN");
        RoleResponse expectedResponse = new RoleResponse(id, "ADMIN");

        when(repository.findById(id)).thenReturn(Optional.of(role));
        when(mapper.toDTO(role)).thenReturn(expectedResponse);

        RoleResponse result = findRoleByIdImpl.findRoleById(id);

        assertNotNull(result);
        assertEquals("ADMIN", result.name());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar RoleNotFoundException quando o ID não existir")
    void shouldThrowExceptionWhenIdDoesNotExist() {
        Long id = 99L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class, () ->
                findRoleByIdImpl.findRoleById(id)
        );

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
        verify(repository).findById(id);
        verifyNoInteractions(mapper);
    }
}