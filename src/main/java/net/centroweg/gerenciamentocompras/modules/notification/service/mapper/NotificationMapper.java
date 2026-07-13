package net.centroweg.gerenciamentocompras.modules.notification.service.mapper;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toEntity(String title, String message, Long userId, Long requestId) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUserId(userId);
        notification.setRequestId(requestId);
        return notification;
    }

    public NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getViewed(),
                notification.getCreatedAt(),
                notification.getUserId(),
                notification.getRequestId()
        );
    }
}
