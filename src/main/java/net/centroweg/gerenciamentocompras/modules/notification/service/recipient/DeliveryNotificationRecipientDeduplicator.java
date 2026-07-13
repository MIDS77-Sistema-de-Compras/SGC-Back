package net.centroweg.gerenciamentocompras.modules.notification.service.recipient;

import net.centroweg.gerenciamentocompras.modules.delivery.service.api.dto.DeliveryNotificationRecipient;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

@Component
public class DeliveryNotificationRecipientDeduplicator {

    public List<Long> distinctUserIds(Collection<DeliveryNotificationRecipient> recipients) {
        var values = new LinkedHashMap<Long, Long>();
        if (recipients != null) recipients.stream().filter(java.util.Objects::nonNull)
                .filter(value -> value.userId() != null).forEach(value -> values.putIfAbsent(value.userId(), value.userId()));
        return List.copyOf(values.values());
    }

    public List<DeliveryNotificationRecipient> distinctEmails(Collection<DeliveryNotificationRecipient> recipients) {
        var values = new LinkedHashMap<String, DeliveryNotificationRecipient>();
        if (recipients != null) recipients.stream().filter(java.util.Objects::nonNull)
                .filter(value -> value.email() != null && !value.email().isBlank()).forEach(value -> {
                    String email = value.email().trim().toLowerCase(Locale.ROOT);
                    values.putIfAbsent(email, new DeliveryNotificationRecipient(value.userId(), value.name(), email));
                });
        return List.copyOf(values.values());
    }
}
