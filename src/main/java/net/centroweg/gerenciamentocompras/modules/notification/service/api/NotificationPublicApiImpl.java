package net.centroweg.gerenciamentocompras.modules.notification.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;

import org.springframework.stereotype.Service;

import java.util.Collection;

import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;

@Service
@RequiredArgsConstructor
public class NotificationPublicApiImpl implements NotificationPublicApi {

    private final CreateInternalNotificationUseCase createInternalNotificationService;

    @Override
    public void createInternalNotifications(
            String title,
            String message,
            NotificationType notificationType,
            Long requestId,
            Collection<Long> userIds
    ) {
        createInternalNotificationService.createNotifications(title, message, notificationType, requestId, userIds);
    }
}
