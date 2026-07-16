package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.domain.exception.NotificationNotFoundException;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por marcar uma {@link Notification} como lida.
 */
@Service
@RequiredArgsConstructor
public class MarkAsViewedServiceImpl {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    /**
     * Marca a notificação como lida e atualiza no banco de dados.
     * @param id identificador da notificação.
     * @return notificação já atualizada.
     */
    public NotificationResponse markAsViewed(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException());
        notification.setViewed(true);
        return notificationMapper.toResponse(notificationRepository.save(notification));
    }
}