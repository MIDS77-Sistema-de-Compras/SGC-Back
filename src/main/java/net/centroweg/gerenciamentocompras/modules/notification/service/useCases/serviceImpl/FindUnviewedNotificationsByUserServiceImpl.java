package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * Caso de uso responsável pela listagem de {@link Notification} não lidas por usuário.
 */
@Service
@RequiredArgsConstructor
public class FindUnviewedNotificationsByUserServiceImpl {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    /**
     * Lista as notificações encontradas na pesquisa específica no banco de dados.
     * @param userId identificador do usuário.
     * @return lista com as notificações encontradas.
     */
    public List<NotificationResponse> findUnviewedByUser(Long userId) {
        return notificationRepository.findByUserIdAndViewedFalse(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }
}
