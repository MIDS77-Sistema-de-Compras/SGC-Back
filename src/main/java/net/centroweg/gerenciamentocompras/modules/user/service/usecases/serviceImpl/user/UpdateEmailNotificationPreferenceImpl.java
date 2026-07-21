package net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceImpl.user;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.UpdateEmailNotificationPreference;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

@Service
@RequiredArgsConstructor
public class UpdateEmailNotificationPreferenceImpl {

    private final UserRepository userRepository;

    @Transactional
    public MessageDTO updatePreference(Long id, UpdateEmailNotificationPreference preference) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setEmailNotificationsEnabled(Boolean.TRUE.equals(preference.enabled()));

        return new MessageDTO(Boolean.TRUE.equals(preference.enabled())
                ? "Notificações por e-mail ativadas."
                : "Notificações por e-mail desativadas.");
    }

}