package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
/**
 * Implementação responsável pelo status de solicitação em análise.
 *
 * <p>Representa que a solicitação foi criada e está
 * sendo analisada pela coordenação ou supervisão.</p>
 *
 * @author André
 * @since 1.0
 */
public class UnderReview  implements StatusIntrf {
    /**
     * Retorna o nome do status.
     *
     * @return nome do status em análise
     */
    @Override
    public String getName() {
        return "Em analise";
    }
    /**
     * Retorna a descrição do status.
     *
     * @return descrição do status em análise
     */
    @Override
    public String getDescription() {
        return "Solicitação em análise";
    }
}
