package net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.request;

import java.time.LocalDateTime;

public record AuditLogFilterRequest(
        String typeAction,
        String agentEmail,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
