package net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.response;

import java.time.LocalDateTime;

public record AuditLogDTOResponse(
        Long id,
        String userAgentName,
        String userAgentRole,
        String typeAction,
        String userTargetName,
        Long request,
        LocalDateTime timestamp
) {
}