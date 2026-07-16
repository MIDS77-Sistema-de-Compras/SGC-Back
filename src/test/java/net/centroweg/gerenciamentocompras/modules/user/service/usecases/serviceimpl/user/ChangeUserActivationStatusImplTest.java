package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.SystemRole;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.ChangeUserActivationStatus;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.authorization.UserRoleAuthorizationService;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user.ChangeUserActivationStatusImpl;
import org.junit.jupiter.api.BeforeEach;
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
class ChangeUserActivationStatusImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRoleAuthorizationService authorizationService;

    @InjectMocks
    private ChangeUserActivationStatusImpl changeUserActivationStatus;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Usuário Teste");
        user.setCpf("cpf-hash");
        user.setEmail("usuario@teste.com");
        user.setPassword("senha-hash");
        user.setExtensionNumber("1234");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        user.setUpdatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        user.setRole(new Role("DOCENTE"));
        user.setProfilePicture("foto.png");
    }

    @Test
    @DisplayName("Deve desativar usuário ativo, atualizar data, salvar e mapear a resposta")
    void shouldDeactivateActiveUser() {
        LocalDateTime previousUpdatedAt = user.getUpdatedAt();
        UserResponse expectedResponse = response(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(expectedResponse);

        UserResponse response = changeUserActivationStatus.changeActivationStatus(
                1L,
                new ChangeUserActivationStatus(false)
        );

        assertSame(expectedResponse, response);
        assertFalse(user.getActive());
        assertTrue(user.getUpdatedAt().isAfter(previousUpdatedAt));
        verify(authorizationService).validateCanChangeActivationStatus(SystemRole.DOCENTE);
        verify(userRepository).save(user);
        verify(userMapper).toDTO(user);
    }

    @Test
    @DisplayName("Deve ativar usuário inativo")
    void shouldActivateInactiveUser() {
        user.setActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        changeUserActivationStatus.changeActivationStatus(
                1L,
                new ChangeUserActivationStatus(true)
        );

        assertTrue(user.getActive());
    }

    @Test
    @DisplayName("Deve manter a operação idempotente quando o estado solicitado já for o atual")
    void shouldBeIdempotentWhenStatusIsUnchanged() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> changeUserActivationStatus.changeActivationStatus(
                1L,
                new ChangeUserActivationStatus(true)
        ));

        assertTrue(user.getActive());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Não deve alterar dados do usuário além do estado e da data de atualização")
    void shouldNotChangeOtherUserFields() {
        Role originalRole = user.getRole();
        LocalDateTime originalCreatedAt = user.getCreatedAt();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenAnswer(invocation -> invocation.getArgument(0));

        changeUserActivationStatus.changeActivationStatus(
                1L,
                new ChangeUserActivationStatus(false)
        );

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("Usuário Teste", saved.getName());
        assertEquals("cpf-hash", saved.getCpf());
        assertEquals("usuario@teste.com", saved.getEmail());
        assertEquals("senha-hash", saved.getPassword());
        assertEquals("1234", saved.getExtensionNumber());
        assertSame(originalRole, saved.getRole());
        assertEquals(originalCreatedAt, saved.getCreatedAt());
        assertEquals("foto.png", saved.getProfilePicture());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não existir")
    void shouldThrowWhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> changeUserActivationStatus.changeActivationStatus(
                        99L,
                        new ChangeUserActivationStatus(false)
                )
        );

        verifyNoInteractions(authorizationService, userMapper);
        verify(userRepository, never()).save(any());
    }

    private UserResponse response(boolean active) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail(),
                user.getExtensionNumber(),
                active,
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfilePicture(),
                user.getRole().getName()
        );
    }
}
