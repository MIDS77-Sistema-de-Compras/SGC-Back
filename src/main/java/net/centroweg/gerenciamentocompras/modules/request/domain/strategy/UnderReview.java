package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

public class UnderReview  implements StatusIntrf {

    @Override
    public String getName() {
        return "Em analise";
    }

    @Override
    public String getDescription() {
        return "Solicitação em análise";
    }
}
