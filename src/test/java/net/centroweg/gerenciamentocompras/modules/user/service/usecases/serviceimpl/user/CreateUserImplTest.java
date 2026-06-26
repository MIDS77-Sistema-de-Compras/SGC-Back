package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimpl.user;

import net.centroweg.gerenciamentocompras.config.security.CpfHasher;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.CreateUserImpl;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user.UniquenessValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserImplTest {

    @Mock private UserRepository repository;
    @Mock private UserMapper mapper;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private CpfHasher cpfHasher;
    @Mock private UniquenessValidator uniquenessValidator;

    @InjectMocks
    private CreateUserImpl createUserImpl;

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        CreateUser dto = new CreateUser(
                "Maria Eduarda", "maria@gmail.com", "12345678900",
                "Senha@123", "1234", true, "COMPRADOR"
        );

        User entity = new User();
        entity.setName("Maria Eduarda");

        UserResponse response = new UserResponse(
                1L, "Maria Eduarda", "12345678900", "maria@gmail.com",
                "1234", true, LocalDateTime.now(), LocalDateTime.now(), null
        );

        when(passwordEncoder.encode(any())).thenReturn("hashed-password");
        when(cpfHasher.hash(any())).thenReturn("hashed-cpf");
        when(roleRepository.findByNameIgnoreCase("COMPRADOR")).thenReturn(Optional.of(new Role("COMPRADOR")));
        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toDTO(any())).thenReturn(response);

        UserResponse result = createUserImpl.createUser(dto);

        assertNotNull(result);
        verify(mapper).toEntity(any());
        verify(repository).save(any());
        verify(mapper).toDTO(any());
    }

    @Test
    @DisplayName("Deve verificar se os dados do DTO estão chegando corretamente na Entidade antes de salvar")
    void deveVerificarSeDadosEstaoSendoPassadosCorretamente() {
        CreateUser request = new CreateUser(
                "João Silva", "joao@email.com", "12345678901",
                "Senha@123", "4321", true, "COMPRADOR"
        );

        User userMapeado = new User();
        userMapeado.setName(request.name());
        userMapeado.setEmail(request.email());
        userMapeado.setCpf(request.cpf());
        userMapeado.setExtensionNumber(request.extensionNumber());
        userMapeado.setActive(request.active());

        when(passwordEncoder.encode(any())).thenReturn("hashed-password");
        when(cpfHasher.hash(any())).thenReturn("hashed-cpf");
        when(roleRepository.findByNameIgnoreCase("COMPRADOR")).thenReturn(Optional.of(new Role("COMPRADOR")));
        when(mapper.toEntity(any(CreateUser.class))).thenReturn(userMapeado);
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        createUserImpl.createUser(request);

        verify(repository).save(userCaptor.capture());
        User userEnviadoParaOBanco = userCaptor.getValue();

        assertEquals(request.name(), userEnviadoParaOBanco.getName(), "O nome foi mandado errado!");
        assertEquals(request.email(), userEnviadoParaOBanco.getEmail(), "O email foi mandado errado!");
        assertEquals(request.cpf(), userEnviadoParaOBanco.getCpf(), "O CPF foi mandado errado!");
        assertEquals(request.extensionNumber(), userEnviadoParaOBanco.getExtensionNumber(), "O ramal foi mandado errado!");
        assertEquals(request.active(), userEnviadoParaOBanco.getActive(), "O status ativo foi mandado errado!");
    }
}
