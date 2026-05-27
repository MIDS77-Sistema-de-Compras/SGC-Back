package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.UpdateUserAllImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateUserAllImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UpdateUserAllImpl updateUserAllImpl; // Injetando a classe correta

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar usuário inexistente")
    void shouldThrowExceptionWhenUserNotFoundOnUpdate() {
        // Arrange
        Long id = 1L;
        // Criando um request fictício (garanta que os campos batam com o seu Record CreateUser)
        CreateUser request = new CreateUser(
                "Novo Nome",
                "email@test.com",
                "12345678901",
                "Senha@123",
                "1234",
                true,
                "ADMIN"
        );

        // Configuramos o mock para retornar Vazio quando buscar esse ID
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            // Chamando o método a partir da instância injetada
            updateUserAllImpl.updateUserAll(id, request);
        });

        // Verifica se a mensagem da exceção é exatamente a que você escreveu na Service
        assertEquals("Usuário não encontrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Deve verificar se o Update está realmente atualizando todos os campos")
    void deveVerificarSeUpdateEstaMandandoDadosCertos() {
        // Arrange
        Long id = 1L;
        CreateUser request = new CreateUser("Novo Nome", "novo@email.com", "111", "S@1", "999", false, "USER");
        User usuarioExistenteNoBanco = new User(); // simulando usuário antigo

        when(repository.findById(id)).thenReturn(java.util.Optional.of(usuarioExistenteNoBanco));
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        updateUserAllImpl.updateUserAll(id, request);

        // Assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(captor.capture());
        User atualizado = captor.getValue();

        assertEquals("Novo Nome", atualizado.getName());
        assertEquals("novo@email.com", atualizado.getEmail());
        // Aqui você validaria se o seu userSave.setUpdatedAt(LocalDateTime.now()) foi chamado também
    }
}