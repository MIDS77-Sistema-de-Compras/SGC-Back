package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.domain.exception.NotificationRecipientNotFoundException;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.recipient.EmailNotificationPreferenceFilter;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNotificationServiceImpl {

    private final CreateInternalNotificationUseCase createInternalNotificationService;
    private final NotificationEmailService notificationEmailService;
    private final UserPublicApi userPublicApi;
    private final EmailNotificationPreferenceFilter preferenceFilter;

    public void createNotification(NotificationRequest request) {
        var savedNotification = createInternalNotificationService.createNotification(request);
        if (savedNotification.isEmpty()) {
            return;
        }

        Notification saved = savedNotification.get();
        var user = userPublicApi.findNotificationDataByIds(java.util.List.of(saved.getUserId())).stream()
                .findFirst()
                .orElseThrow(() -> new NotificationRecipientNotFoundException(saved.getUserId()));

        if (preferenceFilter.isEnabled(saved.getUserId())) {
            notificationEmailService.sendNotificationEmail(
                    user.name(),
                    user.email(),
                    saved.getTitle(),
                    saved.getMessage(),
                    saved.getRequestId()
            );
        }

    }

}
