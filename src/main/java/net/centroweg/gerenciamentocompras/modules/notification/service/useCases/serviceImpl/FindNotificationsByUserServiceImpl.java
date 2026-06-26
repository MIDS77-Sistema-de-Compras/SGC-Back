package net.centroweg.gerenciamentocompras.modules.notification.service.useCases.serviceImpl;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindNotificationsByUserServiceImpl {


    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public List<NotificationResponse> findNotificationsByUser(Long userId) {

        return notificationRepository.findByUserId(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

}
