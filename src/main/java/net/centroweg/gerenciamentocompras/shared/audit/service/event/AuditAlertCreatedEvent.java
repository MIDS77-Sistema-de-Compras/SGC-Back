package net.centroweg.gerenciamentocompras.shared.audit.service.event;

/**
 * Evento publicado quando uma ação de auditoria é classificada como alerta administrativo.
 */
public record AuditAlertCreatedEvent(
        Long auditLogId,
        String action,
        String actorName,
        Long requestId
) {
}
