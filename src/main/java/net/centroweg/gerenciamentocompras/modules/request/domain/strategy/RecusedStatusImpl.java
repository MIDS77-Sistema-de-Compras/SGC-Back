package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
/**
 * Implementação responsável pelo status de solicitação recusada.
 *
 * <p>Representa que a solicitação foi recusada
 * pela coordenação ou supervisão.</p>
 *
 * @author André
 * @since 1.0
 */
public class RecusedStatusImpl implements StatusIntrf {
    /**
     * Retorna o nome do status.
     *
     * @return nome do status recusado
     */
    @Override
    public String getName() {
        return "Recusado";
    }
    /**
     * Retorna a descrição do status.
     *
     * @return descrição do status recusado
     */
    @Override
    public String getDescription() {
        return "Solicitação recusada pela coordenação/supervisão.";
    }
}
