package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.domain.enums.NotificationType;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import net.centroweg.gerenciamentocompras.shared.security.authority.Authorities;

@Service
@RequiredArgsConstructor
public class FindNotificationByOwnUser {


    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public Page<NotificationResponse> findNotificationsByOwnUser(UserPrincipal userPrincipal, Pageable pageable) {
        Page<Notification> notifications = isAdministrator(userPrincipal)
                ? notificationRepository.findByUserIdAndNotificationType(
                        userPrincipal.getId(),
                        NotificationType.ALERTA_ADMINISTRATIVO,
                        pageable
                )
                : notificationRepository.findByUserId(userPrincipal.getId(), pageable);

        return notifications
                .map(notificationMapper::toResponse);
    }

    private boolean isAdministrator(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .anyMatch(authority -> Authorities.ADMIN.equalsIgnoreCase(authority.getAuthority()));
    }
}
