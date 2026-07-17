package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);
    List<NotificationResponse> findNotificationsByUser(Long userId);
    List<NotificationResponse> findUnviewedNotificationsByUser(Long userId);
    NotificationResponse markAsViewed(Long id);
    Page<NotificationResponse> findByOwnUser (UserPrincipal userPrincipal, Pageable pageable);
}
