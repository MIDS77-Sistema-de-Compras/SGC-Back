package net.centroweg.gerenciamentocompras.modules.notification.service.mapper;

import org.springframework.stereotype.Component;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;

@Component
public class NotificationMapper {

    public Notification toEntity(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setTitle(request.title());
        notification.setMessage(request.message());
        notification.setNotificationType(NotificationType.valueOf(request.notificationType()));
        notification.setUserId(request.userId());
        notification.setRequestId(request.requestId());
        return notification;
    }

    public NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getNotificationType().toString(),
                notification.getViewed(),
                notification.getCreatedAt(),
                notification.getUserId(),
                notification.getRequestId()
        );
    }
}
