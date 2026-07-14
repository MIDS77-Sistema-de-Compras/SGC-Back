package net.centroweg.gerenciamentocompras.modules.notification.service.recipient;

import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationRecipient;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class RequestNotificationRecipientDeduplicator {

    public List<Long> distinctUserIds(Collection<RequestNotificationRecipient> recipients) {
        return distinctRecipientsByUserId(recipients).stream().map(RequestNotificationRecipient::userId).toList();
    }

    public List<RequestNotificationRecipient> distinctRecipientsByUserId(Collection<RequestNotificationRecipient> recipients) {
        Map<Long, RequestNotificationRecipient> distinct = new LinkedHashMap<>();
        if (recipients != null) {
            recipients.stream().filter(java.util.Objects::nonNull)
                    .filter(recipient -> recipient.userId() != null)
                    .forEach(recipient -> distinct.putIfAbsent(recipient.userId(), recipient));
        }
        return List.copyOf(distinct.values());
    }

    public List<RequestNotificationRecipient> distinctEmailRecipients(Collection<RequestNotificationRecipient> recipients) {
        Map<String, RequestNotificationRecipient> distinct = new LinkedHashMap<>();
        if (recipients != null) {
            recipients.stream().filter(java.util.Objects::nonNull)
                    .filter(recipient -> recipient.email() != null && !recipient.email().isBlank())
                    .forEach(recipient -> {
                        String email = recipient.email().trim().toLowerCase(Locale.ROOT);
                        distinct.putIfAbsent(email, new RequestNotificationRecipient(
                                recipient.userId(), recipient.userName(), email));
                    });
        }
        return List.copyOf(distinct.values());
    }
}
