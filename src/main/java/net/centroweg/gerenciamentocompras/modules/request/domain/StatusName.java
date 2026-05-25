package net.centroweg.gerenciamentocompras.modules.request.domain;

public enum StatusName {

    AGUARDANDO_APROVACAO("Aguardando aprovação"),
    APROVADO("Aprovado"),
    RECUSADO("Recusado"),
    EM_ATENDIMENTO("Em atendimento"),
    PEDIDO_CANCELADO("Pedido cancelado"),
    ENTREGUE("Entregue"),
    PARCIALMENTE_ATENDIDA("Parcialmente atendida");

    private final String value;

    StatusName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}