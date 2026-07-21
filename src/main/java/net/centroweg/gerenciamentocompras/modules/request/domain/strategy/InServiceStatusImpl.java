package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

/**
 * Implementação responsável pelo status de solicitação em atendimento.
 */
public class InServiceStatusImpl implements StatusIntrf {

    /**
     * Nome do status.
     * @return nome do status em atendimento.
     */
    @Override
    public String getName() {
        return "Em atendimento";
    }

    /**
     * Descrição do status.
     * @return descrição do status em atendimento.
     */
    @Override
    public String getDescription() {
        return "A compra já foi aprovada e o comprador deu andamento ao processo!";
    }
}
