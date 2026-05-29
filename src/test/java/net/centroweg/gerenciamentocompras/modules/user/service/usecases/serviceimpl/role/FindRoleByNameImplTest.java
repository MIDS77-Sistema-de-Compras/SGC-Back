package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.role;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
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

import java.util.List;

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
    @DisplayName("Deve retornar lista de roles pelo nome ignorando maiúsculas/minúsculas")
    void shouldReturnRolesIgnoringCase() {
        String name = "admin";
        Role role = new Role();
        role.setName("ADMIN");
        List<Role> roles = List.of(role);
        List<RoleResponse> expectedResponses = List.of(new RoleResponse(1L, "ADMIN"));

        when(repository.findByNameIgnoringCase(name)).thenReturn(roles);
        when(mapper.toDTOList(roles)).thenReturn(expectedResponses);

        List<RoleResponse> result = findRoleByNameImpl.findRoleByName(name);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("ADMIN", result.get(0).name());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não encontrar roles com o nome informado")
    void shouldReturnEmptyListWhenNoRoleFound() {
        String name = "INEXISTENTE";

        when(repository.findByNameIgnoringCase(name)).thenReturn(List.of());
        when(mapper.toDTOList(List.of())).thenReturn(List.of());

        List<RoleResponse> result = findRoleByNameImpl.findRoleByName(name);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}