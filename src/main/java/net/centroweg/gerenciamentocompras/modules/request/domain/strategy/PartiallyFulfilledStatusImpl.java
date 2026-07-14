package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
/**
 * Implementação responsável pelo status de entrega parcialmente atendida.
 *
 * <p>Representa que a entrega foi concluída, mas nem todos os itens
 * da solicitação foram aprovados/atendidos.</p>
 *
 * @since 1.0
 */
public class PartiallyFulfilledStatusImpl implements StatusIntrf {
    /**
     * Retorna o nome do status.
     *
     * @return nome do status parcialmente atendida
     */
    @Override
    public String getName() {
        return "Parcialmente atendida";
    }
    /**
     * Retorna a descrição do status.
     *
     * @return descrição do status parcialmente atendida
     */
    @Override
    public String getDescription() {
        return "A entrega foi concluída, mas nem todos os itens da solicitação foram atendidos.";
    }
}
