package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;

import java.util.Collection;
import java.util.List;

public interface CreateInternalNotificationUseCase {

    Notification createNotification(NotificationRequest notificationRequest);

    List<Notification> createNotifications(String title, String message, NotificationType notificationType, Long requestId, Collection<Long> userIds);
}
