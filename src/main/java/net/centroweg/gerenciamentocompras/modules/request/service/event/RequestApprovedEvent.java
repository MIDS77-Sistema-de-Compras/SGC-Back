package net.centroweg.gerenciamentocompras.modules.request.service.event;

/**
 * Evento de aplicação publicado quando uma solicitação passa
 * para o status "Aprovado" (na aprovação ou na criação por supervisor).
 *
 * <p>Outros módulos podem reagir a este evento — por exemplo, o módulo
 * de entregas cria a entrega automaticamente.</p>
 *
 * @param requestId identificador da solicitação aprovada
 */
public record RequestApprovedEvent(Long requestId) {
}
