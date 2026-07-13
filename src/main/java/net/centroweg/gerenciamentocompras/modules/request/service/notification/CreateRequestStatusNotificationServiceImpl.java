package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.notification.service.api.NotificationPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
public class CreateRequestStatusNotificationServiceImpl {

    private final RequestRepository requestRepository;
    private final NotificationPublicApi notificationPublicApi;
    private final RequestStatusEmailMessageFactory messageFactory;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNotifications(RequestStatusChangedEvent event) {
        Request request = requestRepository.findWithRequestersById(event.requestId())
                .orElseThrow(RequestNotFoundException::new);

        LinkedHashSet<Long> requesterIds = new LinkedHashSet<>();
        request.getCreatedByUsers().stream()
                .map(User::getId)
                .filter(id -> id != null)
                .forEach(requesterIds::add);

        notificationPublicApi.createInternalNotifications(
                messageFactory.notificationTitle(),
                messageFactory.buildNotificationMessage(event),
                event.requestId(),
                requesterIds
        );
    }
}
