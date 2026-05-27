package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

public class InServiceStatusImpl implements StatusIntrf {

    @Override
    public String getName() {
        return "Em atendimento";
    }

    @Override
    public String getDescription() {
        return "A compra já foi aprovada e o comprador deu andamento ao processo.";
    }
}
