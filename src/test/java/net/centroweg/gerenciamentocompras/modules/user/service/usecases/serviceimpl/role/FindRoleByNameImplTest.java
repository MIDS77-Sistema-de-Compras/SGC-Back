package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.role;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role.FindRoleByNameImpl;
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
class FindRoleByNameImplTest {

    @Mock
    private RoleMapper mapper;

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private FindRoleByNameImpl findRoleByNameImpl;

    @Test
    @DisplayName("Deve retornar role pelo nome ignorando maiúsculas/minúsculas")
    void shouldReturnRoleIgnoringCase() {
        String name = "admin";
        Role role = new Role();
        role.setName("ADMIN");
        RoleResponse expectedResponse = new RoleResponse(1L, "ADMIN");

        when(repository.findByNameIgnoreCase(name)).thenReturn(Optional.of(role));
        when(mapper.toDTO(role)).thenReturn(expectedResponse);

        RoleResponse result = findRoleByNameImpl.findRoleByName(name);

        assertNotNull(result);
        assertEquals("ADMIN", result.name());
    }

    @Test
    @DisplayName("Deve lançar RoleNotFoundException quando não encontrar role com o nome informado")
    void shouldThrowRoleNotFoundExceptionWhenNoRoleFound() {
        String name = "INEXISTENTE";

        when(repository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> findRoleByNameImpl.findRoleByName(name));
    }
}