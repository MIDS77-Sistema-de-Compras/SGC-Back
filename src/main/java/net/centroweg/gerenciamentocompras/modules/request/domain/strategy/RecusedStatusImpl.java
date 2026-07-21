package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

/**
 * Implementação responsável pelo status de solicitação recusada.
 */
public class RecusedStatusImpl implements StatusIntrf {

    /**
     * Nome do status.
     * @return nome do status recusado.
     */
    @Override
    public String getName() {
        return "Recusado";
    }

    /**
     * Descrição do status.
     * @return descrição do status recusado.
     */
    @Override
    public String getDescription() {
        return "Solicitação recusada pela coordenação/supervisão!";
    }
}
