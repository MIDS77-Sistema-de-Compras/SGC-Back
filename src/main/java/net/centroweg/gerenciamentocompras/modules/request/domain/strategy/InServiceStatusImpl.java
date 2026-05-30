package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
/**
 * Implementação responsável pelo status de solicitação em atendimento.
 *
 * <p>Representa que a solicitação já foi aprovada
 * e o comprador iniciou o andamento do processo de compra.</p>
 *
 * @author André
 * @since 1.0
 */
public class InServiceStatusImpl implements StatusIntrf {
    /**
     * Retorna o nome do status.
     *
     * @return nome do status em atendimento
     */

    @Override
    public String getName() {
        return "Em atendimento";
    }
    /**
     * Retorna a descrição do status.
     *
     * @return descrição do status em atendimento
     */
    @Override
    public String getDescription() {
        return "A compra já foi aprovada e o comprador deu andamento ao processo.";
    }
}
