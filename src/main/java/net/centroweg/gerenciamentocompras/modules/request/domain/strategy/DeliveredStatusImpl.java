package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

/**
 * Implementação responsável pelo status de solicitação entregue.
 */
public class DeliveredStatusImpl implements StatusIntrf {

    /**
     * Nome do status.
     * @return nome do status entregue.
     */
    @Override
    public String getName() {
        return "Entregue";
    }
    /**
     * Descrição do status.
     * @return descrição do status entregue.
     */
    @Override
    public String getDescription() {
        return "O processo de compra foi concluído com sucesso e os itens foram entregues!";
    }
}
