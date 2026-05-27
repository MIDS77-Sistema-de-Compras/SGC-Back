package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

public class ApprovedStatusImpl implements StatusIntrf {

    @Override
    public String getName() {
        return "Aprovado";
    }

    @Override
    public String getDescription() {
        return "Solicitação aprovada pela coordenação/supervisão.";
    }
}
