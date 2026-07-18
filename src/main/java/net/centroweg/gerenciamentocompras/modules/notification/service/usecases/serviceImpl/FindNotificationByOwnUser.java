package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;

@Service
@RequiredArgsConstructor
public class FindNotificationByOwnUser {


    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public Page<NotificationResponse> findNotificationsByOwnUser(UserPrincipal userPrincipal, Pageable pageable) {
        return notificationRepository.findByUserId(userPrincipal.getId(), pageable)
                .map(notificationMapper::toResponse);
    }
}
