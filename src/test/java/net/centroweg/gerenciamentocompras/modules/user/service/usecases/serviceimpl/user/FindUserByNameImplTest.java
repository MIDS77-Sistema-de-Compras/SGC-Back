package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.FindUserByNameImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByNameImplTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private FindUserByNameImpl findUserByNameImpl;

    @Test
    @DisplayName("Deve retornar uma lista de usuários ao buscar por nome")
    void shouldReturnListWhenSearchingByName() {
        String name = "João";
        List<User> userList = List.of(new User(), new User());
        List<UserResponse> dtoList = List.of(
                new UserResponse(1L, "João Silva", "...", "...", "...", true, null, null, null),
                new UserResponse(2L, "João Paulo", "...", "...", "...", true, null, null, null)
        );

        when(repository.findByNameIgnoringCase(name)).thenReturn(userList);
        when(mapper.toDTOList(userList)).thenReturn(dtoList);

        List<UserResponse> result = findUserByNameImpl.findUserByName(name);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByNameIgnoringCase(name);
    }
}
