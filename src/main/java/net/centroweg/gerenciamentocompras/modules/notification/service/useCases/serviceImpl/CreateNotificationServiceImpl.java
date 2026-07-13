package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNotificationServiceImpl {

    private final CreateInternalNotificationServiceImpl createInternalNotificationService;
    private final NotificationMapper notificationMapper;
    private final NotificationEmailService notificationEmailService;

    public NotificationResponse createNotification(NotificationRequest request) {
        Notification saved = createInternalNotificationService.createNotification(request);
        User user = saved.getUser();

        notificationEmailService.sendNotificationEmail(
                user.getName(),
                user.getEmail(),
                saved.getTitle(),
                saved.getMessage(),
                saved.getRequest().getId()
        );

        return notificationMapper.toResponse(saved);
    }

}
