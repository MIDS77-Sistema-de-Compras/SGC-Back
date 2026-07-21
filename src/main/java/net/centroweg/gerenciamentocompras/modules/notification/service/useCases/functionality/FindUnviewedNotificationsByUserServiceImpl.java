package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * Caso de uso responsável pela listagem de todas as {@link Notification} não lidas por usuário.
 */
@Service
@RequiredArgsConstructor
public class FindUnviewedNotificationsByUserServiceImpl {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    /**
     * Lista todas as notificações cadastradas no banco de dados não lidas por usuário.
     * @param userId identificador do usuário.
     * @return lista com todas as notificações encontradas, caso exista.
     */
    public List<NotificationResponse> findUnviewedByUser(Long userId) {
        return notificationRepository.findByUserIdAndViewedFalse(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }
}
