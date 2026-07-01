package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceimplm.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.PasswordsDontMatchException;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.ChangePassword;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

@Service
@RequiredArgsConstructor
public class ChangeUserPwdImpl {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MessageDTO changePassword(Long id, ChangePassword changePassword){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if(!passwordEncoder.matches(changePassword.oldPassword(), user.getPassword())){
            throw new PasswordsDontMatchException();
        }

        user.setPassword(passwordEncoder.encode(changePassword.newPassword()));
        return new MessageDTO("Senha alterada com sucesso.");
    }

}
