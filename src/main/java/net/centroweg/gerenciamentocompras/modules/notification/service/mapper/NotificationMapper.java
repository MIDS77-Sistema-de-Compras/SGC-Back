package net.centroweg.gerenciamentocompras.modules.notification.service.mapper;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Component;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;

/**
 * Componente responsável pela conversão entre a entidade({@link Notification}) e seus DTOs de entrada({@link NotificationRequest}) e saída({@link NotificationResponse}).
 */
@Component
public class NotificationMapper {

    /**
     * Converte um DTO de entrada da notificação em uma entidade notificação.
     * @param title titulo da notificação.
     * @param message mensagem da notificação.
     * @param user identificador do usuário.
     * @param request identificador da requisição.
     * @return dados convertidos para entidade.
     */
    public Notification toEntity(String title, String message, User user, Request request) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setUser(user);
        notification.setRequest(request);
        return notification;
    }

    /**
     * Converte uma entidade notificação em um DTO de saída da notificação.
     * @param notification entidade com os dados da notificação.
     * @return dados convertidos em um DTO de saída.
     */
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
