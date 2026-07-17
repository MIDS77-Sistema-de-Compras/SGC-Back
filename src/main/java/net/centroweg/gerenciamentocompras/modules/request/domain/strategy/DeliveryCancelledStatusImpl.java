package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
/**
 * Implementação responsável pelo status de pedido cancelado.
 *
 * <p>Representa que a entrega foi cancelada pelo comprador
 * antes de ser concluída.</p>
 *
 * @since 1.0
 */
public class DeliveryCancelledStatusImpl implements StatusIntrf {
    /**
     * Retorna o nome do status.
     *
     * @return nome do status pedido cancelado
     */
    @Override
    public String getName() {
        return "Pedido cancelado";
    }
    /**
     * Retorna a descrição do status.
     *
     * @return descrição do status pedido cancelado
     */
    @Override
    public String getDescription() {
        return "A entrega foi cancelada pelo comprador antes de ser concluída.";
    }
}
