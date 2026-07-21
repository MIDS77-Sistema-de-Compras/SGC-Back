package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

/**
 * Implementação responsável pelo status de solicitação cancelada.
 */
public class CancelledStatusImpl implements StatusIntrf {

    /**
     * Nome do status.
     * @return nome do status cancelado.
     */
    @Override
    public String getName() {
        return "Cancelado";
    }

    /**
     * Descrição do status.
     * @return descrição do status cancelado.
     */
    @Override
    public String getDescription() {
        return "O pedido de compra foi cancelado ou recusado pelo comprador regional!";
    }
}
