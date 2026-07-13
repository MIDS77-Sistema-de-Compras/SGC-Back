package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
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
public class CreateInternalNotificationServiceImpl {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public Notification createNotification(NotificationRequest notificationRequest) {
        return createNotifications(
                notificationRequest.title(),
                notificationRequest.message(),
                notificationRequest.requestId(),
                List.of(notificationRequest.userId())
        ).getFirst();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Notification> createNotifications(
            String title,
            String message,
            Long requestId,
            Collection<Long> userIds
    ) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(RequestNotFoundException::new);

        LinkedHashSet<Long> distinctIds = new LinkedHashSet<>(userIds);
        distinctIds.remove(null);

        Map<Long, User> usersById = new LinkedHashMap<>();
        userRepository.findAllById(distinctIds)
                .forEach(user -> usersById.put(user.getId(), user));

        List<Notification> notifications = distinctIds.stream()
                .map(userId -> notificationMapper.toEntity(
                        title,
                        message,
                        requireUser(usersById, userId),
                        request
                ))
                .toList();

        return notificationRepository.saveAll(notifications);
    }

    private User requireUser(Map<Long, User> usersById, Long userId) {
        User user = usersById.get(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }
}
