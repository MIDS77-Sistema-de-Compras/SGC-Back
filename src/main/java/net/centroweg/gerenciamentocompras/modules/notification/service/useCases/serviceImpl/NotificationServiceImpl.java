package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceIntrf.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final CreateNotificationServiceImpl createNotificationService;
    private final FindNotificationsByUserServiceImpl findNotificationsByUserService;
    private final FindUnviewedNotificationsByUserServiceImpl findUnviewedNotificationsByUserService;
    private final MarkAsViewedServiceImpl markAsViewedService;
    private final FindNotificationByOwnUser findNotificationByOwnUser;

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        return createNotificationService.createNotification(request);
    }

    @Override
    public List<NotificationResponse> findNotificationsByUser(Long userId) {
        return findNotificationsByUserService.findNotificationsByUser(userId);
    }

    @Override
    public List<NotificationResponse> findUnviewedNotificationsByUser(Long userId) {
        return findUnviewedNotificationsByUserService.findUnviewedByUser(userId);
    }

    @Override
    public NotificationResponse markAsViewed(Long id) {
        return markAsViewedService.markAsViewed(id);
    }

    @Override
    public Page<NotificationResponse> findByOwnUser(UserPrincipal userPrincipal, Pageable pageable){
        return findNotificationByOwnUser.findNotificationsByOwnUser(userPrincipal, pageable);
    }
}
