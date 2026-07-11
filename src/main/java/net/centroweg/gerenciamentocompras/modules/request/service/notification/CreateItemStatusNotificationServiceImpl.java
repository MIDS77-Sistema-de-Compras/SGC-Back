package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateItemStatusNotificationServiceImpl {

    private final RequestRepository requestRepository;
    private final DeliveryRepository deliveryRepository;
    private final NotificationRepository notificationRepository;
    private final ItemStatusEmailMessageFactory messageFactory;

    @Transactional
    public void createNotifications(ItemStatusChangedEvent event) {
        Request request = requestRepository.findWithRequestersById(event.requestId())
                .orElseThrow(RequestNotFoundException::new);
        Optional<Delivery> delivery = findRelatedDelivery(event);
        ItemStatusNotificationContent content = messageFactory.build(event, delivery);

        requesterMap(request).values().forEach(user -> {
            Notification notification = new Notification();
            notification.setTitle(content.title());
            notification.setMessage(content.message());
            notification.setUser(user);
            notification.setRequest(request);
            notificationRepository.save(notification);
        });
    }

    private Optional<Delivery> findRelatedDelivery(ItemStatusChangedEvent event) {
        List<Delivery> deliveries = event.itemType() == RequestItemType.PRODUCT
                ? deliveryRepository.findByRequestIdAndProductItemId(event.requestId(), event.itemId())
                : deliveryRepository.findByRequestIdAndProvisionItemId(event.requestId(), event.itemId());

        return deliveries.stream()
                .filter(delivery -> Boolean.TRUE.equals(delivery.getActive()))
                .findFirst();
    }

    private Map<Long, User> requesterMap(Request request) {
        Map<Long, User> users = new LinkedHashMap<>();
        request.getCreatedByUsers().forEach(user -> users.putIfAbsent(user.getId(), user));
        return users;
    }
}
