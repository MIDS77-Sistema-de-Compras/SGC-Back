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

import java.time.LocalDateTime;
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
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        // 1. DTO de entrada (conforme seu record original)
        CreateUser dto = new CreateUser(
                "Maria Eduarda", "maria@gmail.com", "12345678900",
                "Senha@123", "1234", true, "ADMIN"
        );

        // 2. Entidade que o mapper vai retornar
        User entity = new User();
        entity.setName("Maria Eduarda");

        // 3. Resposta que o mapper vai converter no final
        UserResponse response = new UserResponse(
                1L, "Maria Eduarda", "12345678900", "maria@gmail.com",
                "1234", true, LocalDateTime.now(), LocalDateTime.now()
        );

        // --- CONFIGURAÇÃO DOS MOCKS (Stubbing) ---

        // Usamos doReturn ou any() para garantir que o Mockito não se perca nas referências
        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toDTO(any())).thenReturn(response);

        // --- EXECUÇÃO ---
        UserResponse result = createUserImpl.createUser(dto);

        // --- VERIFICAÇÕES ---
        assertNotNull(result);
        verify(mapper).toEntity(any());
        verify(repository).save(any());
        verify(mapper).toDTO(any());
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

        // ✅ Agora o mapper retorna um User real, não null
        User userMapeado = new User();
        userMapeado.setName(request.name());
        userMapeado.setEmail(request.email());
        userMapeado.setCpf(request.cpf());
        userMapeado.setExtensionNumber(request.extensionNumber());
        userMapeado.setActive(request.active());

        when(mapper.toEntity(any(CreateUser.class))).thenReturn(userMapeado); // ← estava faltando isso
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Act
        createUserImpl.createUser(request);

        // Assert
        verify(repository).save(userCaptor.capture());
        User userEnviadoParaOBanco = userCaptor.getValue();

        assertEquals(request.name(), userEnviadoParaOBanco.getName(), "O nome foi mandado errado!");
        assertEquals(request.email(), userEnviadoParaOBanco.getEmail(), "O email foi mandado errado!");
        assertEquals(request.cpf(), userEnviadoParaOBanco.getCpf(), "O CPF foi mandado errado!");
        assertEquals(request.extensionNumber(), userEnviadoParaOBanco.getExtensionNumber(), "O ramal foi mandado errado!");
        assertEquals(request.active(), userEnviadoParaOBanco.isActive(), "O status ativo foi mandado errado!");
    }

}

