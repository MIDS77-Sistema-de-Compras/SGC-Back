package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final CreateNotificationServiceImpl createNotificationService;
    private final FindNotificationsByUserServiceImpl findNotificationsByUserService;
    private final MarkAsViewedServiceImpl markAsViewedService;

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        return createNotificationService.createNotification(request);
    }

    @Override
    public List<NotificationResponse> findNotificationsByUser(Long userId) {
        return findNotificationsByUserService.findNotificationsByUser(userId);
    }

    @Override
    public NotificationResponse markAsViewed(Long id) {
        return markAsViewedService.markAsViewed(id);
    }

}
