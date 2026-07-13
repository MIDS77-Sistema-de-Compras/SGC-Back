package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.SystemRole;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.UpdateUser;
import net.centroweg.gerenciamentocompras.modules.user.service.authorization.UserRoleAuthorizationService;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.UpdateUserAllImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateUserAllImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRoleAuthorizationService authorizationService;

    @InjectMocks
    private UpdateUserAllImpl updateUserAllImpl;

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar usuário inexistente")
    void shouldThrowExceptionWhenUserNotFoundOnUpdate() {
        Long id = 1L;
        UpdateUser request = new UpdateUser(
                "Novo Nome",
                "email@test.com",
                "Senha@123",
                "1234",
                true,
                "ADMIN"
        );

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                updateUserAllImpl.updateUserAll(id, request)
        );
    }

    @Test
    @DisplayName("Deve atualizar nome, email, senha, ramal e status corretamente")
    void deveAtualizarCamposPermitidosCorretamente() {
        Long id = 1L;
        UpdateUser request = new UpdateUser(
                "Novo Nome",
                "novo@email.com",
                "NovaSenha@123",
                "9999",
                false,
                "SUPERVISOR"
        );

        User usuarioExistenteNoBanco = new User();
        usuarioExistenteNoBanco.setCpf("cpf-hash-original-nao-deve-mudar");
        usuarioExistenteNoBanco.setRole(new Role("DOCENTE"));

        when(repository.findById(id)).thenReturn(Optional.of(usuarioExistenteNoBanco));
        when(roleRepository.findByNameIgnoreCase("SUPERVISOR")).thenReturn(java.util.Optional.of(new Role("SUPERVISOR")));
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(passwordEncoder.encode("NovaSenha@123")).thenReturn("senha-criptografada");

        updateUserAllImpl.updateUserAll(id, request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(captor.capture());
        verify(authorizationService).validateCanEdit(SystemRole.DOCENTE);
        verify(authorizationService).validateCanCreate(SystemRole.SUPERVISOR);
        User atualizado = captor.getValue();

        assertEquals("Novo Nome", atualizado.getName());
        assertEquals("novo@email.com", atualizado.getEmail());
        assertEquals("senha-criptografada", atualizado.getPassword());
        assertEquals("9999", atualizado.getExtensionNumber());
        assertEquals(false, atualizado.getActive());
    }

    @Test
    @DisplayName("Não deve alterar o CPF do usuário durante a atualização")
    void naoDeveAlterarCpfDuranteAtualizacao() {
        Long id = 1L;
        UpdateUser request = new UpdateUser(
                "Outro Nome",
                "outro@email.com",
                "OutraSenha@123",
                "1111",
                true,
                "DOCENTE"
        );

        User usuarioExistenteNoBanco = new User();
        usuarioExistenteNoBanco.setCpf("cpf-hash-original-nao-deve-mudar");
        usuarioExistenteNoBanco.setRole(new Role("DOCENTE"));

        when(repository.findById(id)).thenReturn(Optional.of(usuarioExistenteNoBanco));
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(roleRepository.findByNameIgnoreCase("DOCENTE")).thenReturn(java.util.Optional.of(new Role("DOCENTE")));

        updateUserAllImpl.updateUserAll(id, request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(captor.capture());
        verify(authorizationService).validateCanEdit(SystemRole.DOCENTE);
        verify(authorizationService).validateCanCreate(SystemRole.DOCENTE);
        User atualizado = captor.getValue();

        assertEquals("cpf-hash-original-nao-deve-mudar", atualizado.getCpf());
    }
}
