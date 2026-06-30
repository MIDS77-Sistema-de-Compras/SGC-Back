package net.centroweg.gerenciamentocompras.shared.audit.presentation.dto.request;

public record AuditLogDTORequest(
        String userAgentName,
        String userTargetName,
        String typeAction,
        Long request
) {
}
