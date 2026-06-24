package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.FindLoggedUser;
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
class FindLoggedUserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private FindLoggedUser findLoggedUser;

    @Test
    @DisplayName("Deve retornar o usuário logado com sucesso")
    void deveRetornarUsuarioLogadoComSucesso() {
        User userEntity = new User();
        userEntity.setEmail("test@test.com");
        
        UserPrincipal userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.getUsername()).thenReturn("test@test.com");
        
        UserResponse expectedResponse = new UserResponse(1L, "Teste", "52998224725", "test@test.com", "...", true, null, null, null);
        
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(userEntity));
        when(userMapper.toDTO(userEntity)).thenReturn(expectedResponse);

        UserResponse result = findLoggedUser.findLoggedUser(userPrincipal);

        assertNotNull(result);
        assertEquals("test@test.com", result.email());
        assertEquals("Teste", result.name());
        
        verify(userRepository, times(1)).findByEmail("test@test.com");
        verify(userMapper, times(1)).toDTO(userEntity);
    }

    @Test
    @DisplayName("Deve lançar UserNotFoundException quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        UserPrincipal userPrincipal = mock(UserPrincipal.class);
        when(userPrincipal.getUsername()).thenReturn("notfound@test.com");
        
        when(userRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> findLoggedUser.findLoggedUser(userPrincipal));
        
        verify(userRepository, times(1)).findByEmail("notfound@test.com");
        verifyNoInteractions(userMapper);
    }
}
