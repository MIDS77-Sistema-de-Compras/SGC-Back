package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.ChangeUserActivationStatus;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user.ChangeUserActivationStatusImpl;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private ChangeUserActivationStatusImpl changeUserActivationStatus;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Deve apenas delegar a alteração do estado de ativação ao caso de uso")
    void shouldDelegateActivationStatusChange() {
        Long userId = 1L;
        ChangeUserActivationStatus request = new ChangeUserActivationStatus(false);
        UserResponse expectedResponse = new UserResponse(
                userId,
                "Usuário Teste",
                "cpf-hash",
                "usuario@teste.com",
                "1234",
                false,
                null,
                null,
                null,
                "DOCENTE"
        );
        when(changeUserActivationStatus.changeActivationStatus(userId, request))
                .thenReturn(expectedResponse);

        UserResponse response = userService.changeActivationStatus(userId, request);

        assertSame(expectedResponse, response);
        verify(changeUserActivationStatus).changeActivationStatus(userId, request);
    }
}
