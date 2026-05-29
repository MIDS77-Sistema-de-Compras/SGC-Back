package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.DeleteUserImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteUserImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private DeleteUserImpl deleteUserImpl;

    @Test
    @DisplayName("Deve chamar o repositório para deletar um usuário")
    void shouldInvokeRepositoryToDeleteUser() {
        Long id = 1L;

        // Como o método é void, apenas executamos
        deleteUserImpl.deleteUser(id);

        // Verificamos se o método deleteById foi chamado exatamente 1 vez com o ID correto
        verify(repository, times(1)).deleteById(id);
    }
}