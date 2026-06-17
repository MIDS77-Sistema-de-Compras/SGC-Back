package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.FindUserByIdImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindUserByIdImplTest {
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private FindUserByIdImpl findUserByIdImpl;

    @Test
    @DisplayName("Deve retornar um usuário quando o ID existir")
    void shouldReturnUserWhenIdExists() {
        Long id = 1L;
        User user = new User();
        UserResponse expectedResponse = new UserResponse(id, "Teste", "000", "e@e.com", "123", true, null, null, null);

        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(mapper.toDTO(user)).thenReturn(expectedResponse);

        UserResponse result = findUserByIdImpl.findUserById(id);

        assertNotNull(result);
        assertEquals("Teste", result.name());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID não existir")
    void shouldThrowExceptionWhenIdDoesNotExist() {
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                findUserByIdImpl.findUserById(id)
        );

        assertTrue(exception.getMessage().contains("Usuário não encontrado com id:"));
    }

}

