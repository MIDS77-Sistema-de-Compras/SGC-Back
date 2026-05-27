package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

public class DeliveredStatusImpl implements StatusIntrf {

    @Override
    public String getName() {
        return "Entregue";
    }

    @Override
    public String getDescription() {
        return "O processo de compra foi concluído com sucesso e os itens foram entregues.";
    }
}
