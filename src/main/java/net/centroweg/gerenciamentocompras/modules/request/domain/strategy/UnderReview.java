package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

/**
 * Implementação responsável pelo status de solicitação em análise.
 */
public class UnderReview  implements StatusIntrf {

    /**
     * Nome do status.
     * @return nome do status em análise.
     */
    @Override
    public String getName() {
        return "Em analise";
    }

    /**
     * Descrição do status.
     * @return descrição do status em análise.
     */
    @Override
    public String getDescription() {
        return "Solicitação em análise!";
    }
}
