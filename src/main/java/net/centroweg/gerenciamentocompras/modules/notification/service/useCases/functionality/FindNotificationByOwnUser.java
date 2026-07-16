package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.functionality;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;

/**
 * Caso de uso responsável pela listagem de {@link Notification} pelo usuário responsável pela solicitação.
 */
@Service
@RequiredArgsConstructor
public class FindNotificationByOwnUser {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    /**
     * Lista as todas notificações cadastradas no banco de dados pelo usuário responsável.
     * @param userPrincipal usuário responsável.
     * @return lista com todas as notificações encontrados.
     */
    public List<NotificationResponse> findNotificationsByOwnUser(UserPrincipal userPrincipal) {
        return notificationRepository.findByUserId(userPrincipal.getId())
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }
}
