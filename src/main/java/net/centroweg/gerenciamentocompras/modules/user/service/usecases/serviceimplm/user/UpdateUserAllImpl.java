package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.RoleNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.RoleRepository;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Atualizar usuário com o identificador único informado
 */


@Service
@RequiredArgsConstructor
public class UpdateUserAllImpl {

    /**
     * Injeção de dependências
     */

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Método que atualiza usuário com o identificador único
     * @param user dados do usuário
     * @param id identificador único do usuário
     * @return usuário já atualizado
     */

    public UserResponse updateUserAll(Long id, CreateUser user){
        User userSave = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        Role role = roleRepository.findByNameIgnoreCase(user.nameRole())
                .orElseThrow(() -> new RoleNotFoundException(user.nameRole()));
        String encryptedPassword = passwordEncoder.encode(user.password());


        userSave.setName(user.name());
        userSave.setCpf(user.cpf());
        userSave.setEmail(user.email());
        userSave.setPassword(user.password());
        userSave.setExtensionNumber(user.extensionNumber());
        userSave.setActive(user.active());
        userSave.setUpdatedAt(LocalDateTime.now());
        userSave.setRole(role);

        return userMapper.toDTO(userRepository.save(userSave));
    }
}
