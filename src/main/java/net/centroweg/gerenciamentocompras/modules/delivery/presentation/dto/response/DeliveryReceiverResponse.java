package net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response;

import java.time.LocalDateTime;

public record DeliveryReceiverResponse(
        Long userId,
        String userName,
        String userEmail,
        String extensionNumber,
        Boolean confirmed,
        LocalDateTime confirmedAt,
        String observation
) {
}
