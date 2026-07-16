package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.domain.exception.NotificationRecipientNotFoundException;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.service.api.UserPublicApi;
import net.centroweg.gerenciamentocompras.modules.user.service.api.dto.UserNotificationData;
import net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceIntrf.CreateInternalNotificationUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Persiste notificações internas sem acionar o envio genérico de e-mail.
 */
@Service
@RequiredArgsConstructor
public class CreateInternalNotificationServiceImpl implements CreateInternalNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final UserPublicApi userPublicApi;
    private final RequestPublicApi requestPublicApi;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public Notification createNotification(NotificationRequest notificationRequest) {
        return createNotifications(
                notificationRequest.title(),
                notificationRequest.message(),
                notificationRequest.requestId(),
                List.of(notificationRequest.userId())
        ).getFirst();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Notification> createNotifications(
            String title,
            String message,
            Long requestId,
            Collection<Long> userIds
    ) {
        requestPublicApi.findNotificationDataById(requestId);

        LinkedHashSet<Long> distinctIds = new LinkedHashSet<>(userIds);
        distinctIds.remove(null);

        Map<Long, UserNotificationData> usersById = new LinkedHashMap<>();
        userPublicApi.findNotificationDataByIds(distinctIds)
                .forEach(user -> usersById.put(user.userId(), user));

        List<Notification> notifications = distinctIds.stream()
                .map(userId -> notificationMapper.toEntity(
                        title,
                        message,
                        requireUser(usersById, userId).userId(),
                        requestId
                ))
                .toList();

        return notificationRepository.saveAll(notifications);
    }

    private UserNotificationData requireUser(Map<Long, UserNotificationData> usersById, Long userId) {
        UserNotificationData user = usersById.get(userId);
        if (user == null) {
            throw new NotificationRecipientNotFoundException(userId);
        }
        return user;
    }
}
