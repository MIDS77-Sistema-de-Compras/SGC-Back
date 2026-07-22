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
    NOTIFICACAO_TESTE ("NOTIFICACAO_TESTE"), // apenas para casos de testes
    ALERTA_ADMINISTRATIVO("ALERTA_ADMINISTRATIVO");

    /**
     * Valor textual do tipo, usado ao expor a notificação nos DTOs de request/response.
     */
    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    /**
     * Retorna o valor textual do tipo de notificação.
     *
     * @return o valor textual associado a este tipo
     */
    @Override
    public String toString() {
        return value;
    }
}
