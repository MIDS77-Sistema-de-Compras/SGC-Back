package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
/**
 * Implementação responsável pelo status de solicitação entregue.
 *
 * <p>Representa que o processo de compra foi concluído
 * e os itens foram entregues com sucesso.</p>
 *
 * @author André
 * @since 1.0
 */
public class DeliveredStatusImpl implements StatusIntrf {
    /**
     * Retorna o nome do status.
     *
     * @return nome do status entregue
     */
    @Override
    public String getName() {
        return "Entregue";
    }
    /**
     * Retorna a descrição do status.
     *
     * @return descrição do status entregue
     */
    @Override
    public String getDescription() {
        return "O processo de compra foi concluído com sucesso e os itens foram entregues.";
    }
}
