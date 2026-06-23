package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);
    List<NotificationResponse> findNotificationsByUser(Long userId);
    List<NotificationResponse> findUnviewedNotificationsByUser(Long userId);
    NotificationResponse markAsViewed(Long id);
    List<NotificationResponse> findByOwnUser (UserPrincipal userPrincipal);
}
