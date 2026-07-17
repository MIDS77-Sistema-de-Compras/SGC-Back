package net.centroweg.gerenciamentocompras.modules.request.service.api.dto;

import java.util.List;

public record RequestNotificationData(
        Long requestId,
        List<RequestNotificationRecipient> recipients
) {
    public RequestNotificationData {
        recipients = recipients == null ? List.of() : List.copyOf(recipients);
    }
}
