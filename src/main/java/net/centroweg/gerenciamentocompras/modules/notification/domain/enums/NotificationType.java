package net.centroweg.gerenciamentocompras.modules.notification.domain.enums;

/**
 * Quatro principais notificações do sistema, orientadas à evento.
 * 
 * @author gabrielEFagundes
 * @since 20/07/2026
 */
public enum NotificationType {
    STATUS_ALTERADO("STATUS_ALTERADO"),
    ITEM_PARA_RETIRADA("ITEM_PARA_RETIRADA"),
    ENTREGA_CRIADA("ENTREGA_CRIADA"),
    SOLICITACAO_VINCULADA_CR("SOLICITACAO_VINCULADA_CR"),
    NOTIFICACAO_TESTE ("NOTIFICACAO_TESTE"); // apenas para casos de testes

    NotificationType(String name) {}
}
