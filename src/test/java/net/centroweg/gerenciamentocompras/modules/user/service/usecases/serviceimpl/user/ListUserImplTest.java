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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    @DisplayName("Deve retornar uma página com todos os usuários cadastrados")
    void deveRetornarListaDeUsuariosComSucesso() {
        Pageable pageable = Pageable.unpaged();
        User user1 = new User();
        User user2 = new User();

        UserResponse resp1 = new UserResponse(1L, "User 1", "...", "...", "...", true, null, null, null);
        UserResponse resp2 = new UserResponse(2L, "User 2", "...", "...", "...", true, null, null, null);

        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user1, user2)));
        when(mapper.toDTO(user1)).thenReturn(resp1);
        when(mapper.toDTO(user2)).thenReturn(resp2);

        Page<UserResponse> result = listUserImpl.listUser(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("User 1", result.getContent().get(0).name());

        verify(repository, times(1)).findAll(pageable);
        verify(mapper, times(1)).toDTO(user1);
        verify(mapper, times(1)).toDTO(user2);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver usuários no banco")
    void deveRetornarListaVaziaQuandoNaoHouverUsuarios() {
        Pageable pageable = Pageable.unpaged();

        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

        Page<UserResponse> result = listUserImpl.listUser(pageable);

        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(mapper);
    }
}
