package net.centroweg.gerenciamentocompras.modules.notification.service.usecases.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.notification.presentation.dto.response.NotificationResponse;
import net.centroweg.gerenciamentocompras.modules.notification.service.mapper.NotificationMapper;

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
