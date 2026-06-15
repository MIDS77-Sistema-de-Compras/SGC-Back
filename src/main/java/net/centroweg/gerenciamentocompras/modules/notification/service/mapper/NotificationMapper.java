package net.centroweg.gerenciamentocompras.modules.notification.service.mapper;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toEntity(String title, String message, User user, Request request) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUser(user);
        notification.setRequest(request);
        return notification;
    }

    public NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getViewed(),
                notification.getCreatedAt(),
                notification.getUser().getId(),
                notification.getRequest().getId()
        );
    }
}
