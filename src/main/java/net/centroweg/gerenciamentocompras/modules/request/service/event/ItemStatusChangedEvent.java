package net.centroweg.gerenciamentocompras.modules.request.service.event;

import java.time.LocalDateTime;

public record ItemStatusChangedEvent(
        Long requestId,
        Long itemId,
        RequestItemType itemType,
        String itemName,
        String itemCode,
        Double quantity,
        String measurementUnit,
        String previousStatusName,
        String newStatusName,
        String observation,
        LocalDateTime changedAt
) {
}
