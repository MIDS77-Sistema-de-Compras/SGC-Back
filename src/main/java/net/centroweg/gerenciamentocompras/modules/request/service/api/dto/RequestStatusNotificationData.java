package net.centroweg.gerenciamentocompras.modules.request.service.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RequestStatusNotificationData(
        Long requestId,
        String crName,
        String crCode,
        String branchName,
        String currentStatusName,
        LocalDateTime requestDate,
        List<RequestNotificationRecipient> recipients
) {
    public RequestStatusNotificationData {
        recipients = recipients == null ? List.of() : List.copyOf(recipients);
    }
}
