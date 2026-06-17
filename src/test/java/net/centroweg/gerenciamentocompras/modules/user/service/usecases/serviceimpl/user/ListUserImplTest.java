package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.ListUserImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUserImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private ListUserImpl listUserImpl;

    @Test
    @DisplayName("Deve retornar uma lista com todos os usuários cadastrados")
    void deveRetornarListaDeUsuariosComSucesso() {
        // Arrange (Preparação)
        User user1 = new User();
        User user2 = new User();
        List<User> mockUsers = List.of(user1, user2);

        UserResponse resp1 = new UserResponse(1L, "User 1", "...", "...", "...", true, null, null, null);
        UserResponse resp2 = new UserResponse(2L, "User 2", "...", "...", "...", true, null, null, null);
        List<UserResponse> expectedResponse = List.of(resp1, resp2);

        // Quando o repository for chamado, retorna a lista de entidades
        when(repository.findAll()).thenReturn(mockUsers);
        // Quando o mapper for chamado com a lista de entidades, retorna a lista de DTOs
        when(mapper.toDTOList(mockUsers)).thenReturn(expectedResponse);

        // Act (Execução)
        List<UserResponse> result = listUserImpl.listUser();

        // Assert (Verificação)
        assertNotNull(result);
        assertEquals(2, result.size(), "A lista deve conter exatamente 2 usuários");
        assertEquals("User 1", result.get(0).name());

        // Verifica se o repositório foi consultado exatamente uma vez
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver usuários no banco")
    void deveRetornarListaVaziaQuandoNaoHouverUsuarios() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(mapper.toDTOList(anyList())).thenReturn(Collections.emptyList());

        // Act
        List<UserResponse> result = listUserImpl.listUser();

        // Assert
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }
}