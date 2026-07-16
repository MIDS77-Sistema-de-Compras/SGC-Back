package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * Caso de uso responsável pela listagem de {@link Notification} por usuário.
 */
@Service
@RequiredArgsConstructor
public class FindNotificationsByUserServiceImpl {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    /**
     * Lista todas as notificações cadastradas no banco de dados por usuário.
     * @param userId identificador do usuário.
     * @return lista todas as notificações encontradas.
     */
    public List<NotificationResponse> findNotificationsByUser(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

}
