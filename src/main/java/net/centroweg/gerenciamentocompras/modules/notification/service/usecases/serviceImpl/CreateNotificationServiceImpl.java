package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.domain.exception.NotificationRecipientNotFoundException;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNotificationServiceImpl {

    private final CreateInternalNotificationUseCase createInternalNotificationService;
    private final NotificationMapper notificationMapper;
    private final NotificationEmailService notificationEmailService;
    private final UserPublicApi userPublicApi;

    public NotificationResponse createNotification(NotificationRequest request) {
        Notification saved = createInternalNotificationService.createNotification(request);
        var user = userPublicApi.findNotificationDataByIds(java.util.List.of(saved.getUserId())).stream()
                .findFirst()
                .orElseThrow(() -> new NotificationRecipientNotFoundException(saved.getUserId()));

        notificationEmailService.sendNotificationEmail(
                user.name(),
                user.email(),
                saved.getTitle(),
                saved.getMessage(),
                saved.getRequestId()
        );

        return notificationMapper.toResponse(saved);
    }

}
