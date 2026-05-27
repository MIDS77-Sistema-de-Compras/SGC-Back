package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

public class RecusedStatusImpl implements StatusIntrf {

    @Override
    public String getName() {
        return "Recusado";
    }

    @Override
    public String getDescription() {
        return "Solicitação recusada pela coordenação/supervisão.";
    }
}
