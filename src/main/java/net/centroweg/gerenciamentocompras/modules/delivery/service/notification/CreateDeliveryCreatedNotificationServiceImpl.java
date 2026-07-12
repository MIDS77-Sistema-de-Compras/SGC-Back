package net.centroweg.gerenciamentocompras.modules.delivery.service.notification;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import net.centroweg.gerenciamentocompras.modules.notification.domain.entity.Notification;
import net.centroweg.gerenciamentocompras.modules.notification.infrastructure.persistence.NotificationRepository;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateDeliveryCreatedNotificationServiceImpl {

    private final DeliveryRepository deliveryRepository;
    private final NotificationRepository notificationRepository;
    private final DeliveryCreatedEmailMessageFactory messageFactory;

    @Transactional
    public void createNotifications(DeliveryCreatedEvent event) {
        Delivery delivery = deliveryRepository.findById(event.deliveryId())
                .orElseThrow(DeliveryNotFoundException::new);
        DeliveryCreatedNotificationContent content = messageFactory.build(delivery);

        receiverMap(delivery).values().forEach(user -> {
            Notification notification = new Notification();
            notification.setTitle(content.title());
            notification.setMessage(content.message());
            notification.setUser(user);
            notification.setRequest(delivery.getRequest());
            notificationRepository.save(notification);
        });
    }

    private Map<Long, User> receiverMap(Delivery delivery) {
        Map<Long, User> users = new LinkedHashMap<>();
        delivery.getReceivers().forEach(receiver -> users.putIfAbsent(receiver.getUser().getId(), receiver.getUser()));
        return users;
    }
}
