package net.centroweg.gerenciamentocompras.modules.delivery.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.DeliveryNotFoundException;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.delivery.service.event.DeliveryCreatedEvent;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendDeliveryCreatedEmailServiceImpl {

    private final DeliveryRepository deliveryRepository;
    private final EmailSenderService emailSenderService;
    private final DeliveryCreatedEmailMessageFactory messageFactory;

    @Transactional(readOnly = true)
    public void sendEmails(DeliveryCreatedEvent event) {
        Delivery delivery = deliveryRepository.findById(event.deliveryId())
                .orElseThrow(DeliveryNotFoundException::new);
        DeliveryCreatedNotificationContent content = messageFactory.build(delivery);

        receiverMap(delivery).values().forEach(user -> sendToUser(user, content));
    }

    private void sendToUser(User user, DeliveryCreatedNotificationContent content) {
        if (!hasText(user.getEmail())) {
            log.info("Receiver {} has no email. Internal notification was kept.", user.getId());
            return;
        }

        try {
            emailSenderService.sendEmail(
                    new DefaultEmail(content.emailSubject(), user.getEmail()),
                    content.emailHtml()
            );
        } catch (Exception exception) {
            log.error("Error sending delivery created email to {}", user.getEmail(), exception);
        }
    }

    private Map<String, User> receiverMap(Delivery delivery) {
        Map<String, User> users = new LinkedHashMap<>();
        delivery.getReceivers().forEach(receiver -> {
            User user = receiver.getUser();
            String key = hasText(user.getEmail())
                    ? user.getEmail().toLowerCase(Locale.ROOT)
                    : String.valueOf(user.getId());
            users.putIfAbsent(key, user);
        });
        return users;
    }

    private boolean hasText(String text) {
        return text != null && !text.isBlank();
    }
}
