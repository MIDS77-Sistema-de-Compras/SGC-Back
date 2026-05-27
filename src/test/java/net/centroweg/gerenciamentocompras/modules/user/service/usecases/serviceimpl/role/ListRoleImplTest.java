package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.role;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role.ListRoleImpl;
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
class ListRoleImplTest {

    @Mock
    private RoleMapper mapper;

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private ListRoleImpl listRoleImpl;

    @Test
    @DisplayName("Deve retornar todas as roles cadastradas")
    void shouldReturnAllRoles() {
        Role roleAdmin = new Role();
        roleAdmin.setName("ADMIN");
        Role roleUser = new Role();
        roleUser.setName("USER");

        List<Role> roles = List.of(roleAdmin, roleUser);
        List<RoleResponse> expectedResponses = List.of(
                new RoleResponse(1L, "ADMIN"),
                new RoleResponse(2L, "USER")
        );

        when(repository.findAll()).thenReturn(roles);
        when(mapper.toDTOList(roles)).thenReturn(expectedResponses);

        List<RoleResponse> result = listRoleImpl.listRole();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver roles cadastradas")
    void shouldReturnEmptyListWhenNoRolesExist() {
        when(repository.findAll()).thenReturn(List.of());
        when(mapper.toDTOList(List.of())).thenReturn(List.of());

        List<RoleResponse> result = listRoleImpl.listRole();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}