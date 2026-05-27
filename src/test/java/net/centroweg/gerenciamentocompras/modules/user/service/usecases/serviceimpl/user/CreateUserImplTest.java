package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.CreateUserImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserImplTest {
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private CreateUserImpl createUserImpl;

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void shouldCreateUserWithSuccess() {
        // Arrange
        CreateUser request = new CreateUser("João", "joao@email.com", "12345678901", "Senha@123", "1234", true, "ADMIN");
        User userEntity = new User();
        User savedEntity = new User();
        UserResponse expectedResponse = new UserResponse(1L, "João", "12345678901", "joao@email.com", "1234", true, null, null);

        when(mapper.toEntity(request)).thenReturn(userEntity);
        when(repository.save(userEntity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(expectedResponse);

        // Act
        UserResponse result = createUserImpl.createUser(request);

        // Assert
        assertNotNull(result);
        assertEquals("João", result.name());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve verificar se os dados do DTO estão chegando corretamente na Entidade antes de salvar")
    void deveVerificarSeDadosEstaoSendoPassadosCorretamente() {
        // Arrange
        CreateUser request = new CreateUser(
                "João Silva",
                "joao@email.com",
                "12345678901",
                "Senha@123",
                "4321",
                true,
                "ADMIN"
        );

        // Configuramos o mock do repository para retornar qualquer User quando salvar
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Criamos um "Capturador" para o objeto User
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Act
        createUserImpl.createUser(request);

        // Assert
        // Capturamos o que foi enviado para o metodo repository.save()
        verify(repository).save(userCaptor.capture());
        User userEnviadoParaOBanco = userCaptor.getValue();

        // Agora verificamos se o Mapper realmente mandou os dados certos para a Entidade
        assertEquals(request.name(), userEnviadoParaOBanco.getName(), "O nome foi mandado errado!");
        assertEquals(request.email(), userEnviadoParaOBanco.getEmail(), "O email foi mandado errado!");
        assertEquals(request.cpf(), userEnviadoParaOBanco.getCpf(), "O CPF foi mandado errado!");
        assertEquals(request.extensionNumber(), userEnviadoParaOBanco.getExtensionNumber(), "O ramal foi mandado errado!");
        assertEquals(request.active(), userEnviadoParaOBanco.isActive(), "O status ativo foi mandado errado!");
    }

}

