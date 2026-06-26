package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.email.NotificationEmailService;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.request.NotificationRequest;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.modules.user.domain.exception.UserNotFoundException;
import net.centroweg.gerenciamentocompras.modules.user.infrastructure.persistence.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNotificationServiceImpl {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationEmailService notificationEmailService;

    public NotificationResponse createNotification(NotificationRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(request.userId()));

        Request relatedRequest = requestRepository.findById(request.requestId())
                .orElseThrow(() -> new RequestNotFoundException());

        Notification notification = notificationMapper.toEntity(request.title(), request.message(), user, relatedRequest);
        Notification saved = notificationRepository.save(notification);

        notificationEmailService.sendNotificationEmail(
                user,
                notification.getTitle(),
                notification.getMessage(),
                relatedRequest
        );

        return notificationMapper.toResponse(saved);
    }

}
