package net.centroweg.gerenciamentocompras.modules.user.service;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.presentation.controller.UserController;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.UserIntrf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserIntrf userIntrf;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Deve retornar o usuário logado e status 200 OK")
    void deveRetornarUsuarioLogado() {
        // Arrange
        UserPrincipal mockPrincipal = mock(UserPrincipal.class);
        UserResponse expectedResponse = new UserResponse(1L, "Admin Teste", "admin@teste.com", "...", "...", true, null, null, null);
        
        when(userIntrf.findLoggedUser(mockPrincipal)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<UserResponse> response = userController.findLoggedUser(mockPrincipal);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        
        verify(userIntrf, times(1)).findLoggedUser(mockPrincipal);
    }
}
