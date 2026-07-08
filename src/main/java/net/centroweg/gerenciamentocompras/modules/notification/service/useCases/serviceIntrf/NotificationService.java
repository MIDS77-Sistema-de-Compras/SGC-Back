package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);
    List<NotificationResponse> findNotificationsByUser(Long userId);
    List<NotificationResponse> findUnviewedNotificationsByUser(Long userId);
    NotificationResponse markAsViewed(Long id);
    Page<NotificationResponse> findByOwnUser (UserPrincipal userPrincipal, Pageable pageable);
}
