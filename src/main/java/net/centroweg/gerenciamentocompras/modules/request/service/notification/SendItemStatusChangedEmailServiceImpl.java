package net.centroweg.gerenciamentocompras.modules.request.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.entity.Delivery;
import net.centroweg.gerenciamentocompras.modules.delivery.infrastructure.persistence.DeliveryRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.email.model.DefaultEmail;
import net.centroweg.gerenciamentocompras.shared.email.service.EmailSenderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendItemStatusChangedEmailServiceImpl {

    private final RequestRepository requestRepository;
    private final DeliveryRepository deliveryRepository;
    private final EmailSenderService emailSenderService;
    private final ItemStatusEmailMessageFactory messageFactory;

    @Transactional(readOnly = true)
    public void sendEmails(ItemStatusChangedEvent event) {
        Request request = requestRepository.findWithRequestersById(event.requestId())
                .orElseThrow(RequestNotFoundException::new);
        Optional<Delivery> delivery = findRelatedDelivery(event);
        ItemStatusNotificationContent content = messageFactory.build(event, delivery);

        requesterMap(request).values().forEach(user -> sendToUser(user, content));
    }

    private Optional<Delivery> findRelatedDelivery(ItemStatusChangedEvent event) {
        List<Delivery> deliveries = event.itemType() == RequestItemType.PRODUCT
                ? deliveryRepository.findByRequestIdAndProductItemId(event.requestId(), event.itemId())
                : deliveryRepository.findByRequestIdAndProvisionItemId(event.requestId(), event.itemId());

        return deliveries.stream()
                .filter(delivery -> Boolean.TRUE.equals(delivery.getActive()))
                .findFirst();
    }

    private void sendToUser(User user, ItemStatusNotificationContent content) {
        if (!hasText(user.getEmail())) {
            log.info("Requester {} has no email. Internal notification was kept.", user.getId());
            return;
        }

        try {
            emailSenderService.sendEmail(
                    new DefaultEmail(content.emailSubject(), user.getEmail()),
                    content.emailHtml()
            );
        } catch (Exception exception) {
            log.error("Error sending item status email to {}", user.getEmail(), exception);
        }
    }

    private Map<String, User> requesterMap(Request request) {
        Map<String, User> users = new LinkedHashMap<>();
        request.getCreatedByUsers().forEach(user -> {
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
