package net.centroweg.gerenciamentocompras.modules.request.service.event;

import java.time.LocalDateTime;

/**
 * Evento imutável publicado quando o status geral de uma solicitação é realmente alterado.
 */
public record RequestStatusChangedEvent(
        Long requestId,
        Long previousStatusId,
        String previousStatusName,
        Long newStatusId,
        String newStatusName,
        String justification,
        Long changedByUserId,
        String changedByUserName,
        LocalDateTime changedAt
) {
}
