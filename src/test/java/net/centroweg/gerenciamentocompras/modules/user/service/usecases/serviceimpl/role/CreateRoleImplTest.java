package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.role;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.RoleMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.role.CreateRoleImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRoleImplTest {

    @Mock
    private RoleMapper mapper;

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private CreateRoleImpl createRoleImpl;

    @Test
    @DisplayName("Deve criar uma role e retornar o DTO de resposta")
    void shouldCreateRoleAndReturnResponse() {
        CreateRole request = new CreateRole("ADMIN");
        Role entity = new Role();
        entity.setName("ADMIN");
        RoleResponse expectedResponse = new RoleResponse(1L, "ADMIN");

        when(mapper.toEntity(any(CreateRole.class))).thenReturn(entity);
        when(repository.save(any(Role.class))).thenReturn(entity);
        when(mapper.toDTO(any(Role.class))).thenReturn(expectedResponse);

        RoleResponse result = createRoleImpl.createRole(request);

        assertNotNull(result);
        assertEquals("ADMIN", result.name());
        verify(repository).save(any(Role.class));
    }

    @Test
    @DisplayName("Deve verificar se os dados do DTO estão sendo passados corretamente para a entidade")
    void shouldPassCorrectDataToEntity() {
        CreateRole request = new CreateRole("MANAGER");
        Role entity = new Role();
        entity.setName("MANAGER");
        RoleResponse response = new RoleResponse(1L, "MANAGER");

        when(mapper.toEntity(any(CreateRole.class))).thenReturn(entity);
        when(repository.save(any(Role.class))).thenAnswer(i -> i.getArguments()[0]);
        when(mapper.toDTO(any(Role.class))).thenReturn(response);

        createRoleImpl.createRole(request);

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
        verify(repository).save(captor.capture());

        assertEquals("MANAGER", captor.getValue().getName());
    }
}
