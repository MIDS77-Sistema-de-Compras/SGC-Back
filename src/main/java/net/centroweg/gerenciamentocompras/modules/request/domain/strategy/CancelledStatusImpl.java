package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
/**
 * Implementação responsável pelo status de solicitação cancelada.
 *
 * <p>Representa que o pedido de compra foi cancelado
 * ou recusado pelo comprador regional.</p>
 *
 * @author André
 * @since 1.0
 */
public class CancelledStatusImpl implements StatusIntrf {
    /**
     * Retorna o nome do status.
     *
     * @return nome do status cancelado
     */
    @Override
    public String getName() {
        return "Cancelado";
    }

    /**
     * Retorna a descrição do status.
     *
     * @return descrição do status cancelado
     */
    @Override
    public String getDescription() {
        return "O pedido de compra foi cancelado ou recusado pelo comprador regional.";
    }
}
