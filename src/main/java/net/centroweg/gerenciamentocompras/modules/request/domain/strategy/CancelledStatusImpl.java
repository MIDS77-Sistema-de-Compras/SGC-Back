package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

public class CancelledStatusImpl implements StatusIntrf {

    @Override
    public String getName() {
        return "Cancelado";
    }

    @Override
    public String getDescription() {
        return "O pedido de compra foi cancelado ou recusado pelo comprador regional.";
    }
}
