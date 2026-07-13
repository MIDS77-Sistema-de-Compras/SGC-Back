package net.centroweg.gerenciamentocompras.modules.notification.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl.CreateInternalNotificationServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class NotificationPublicApiImpl implements NotificationPublicApi {

    private final CreateInternalNotificationServiceImpl createInternalNotificationService;

    @Override
    public void createInternalNotifications(
            String title,
            String message,
            Long requestId,
            Collection<Long> userIds
    ) {
        createInternalNotificationService.createNotifications(title, message, requestId, userIds);
    }
}
