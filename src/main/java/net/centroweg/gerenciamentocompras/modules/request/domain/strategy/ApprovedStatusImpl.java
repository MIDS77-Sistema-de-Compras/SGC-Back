package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
/**
 * Implementação responsável pelo status de solicitação aprovada.
 *
 * <p>Representa que a solicitação foi aprovada pela
 * coordenação ou supervisão.</p>
 *
 * @author André
 * @since 1.0
 */
public class ApprovedStatusImpl implements StatusIntrf {
    /**
     * Retorna o nome do status.
     *
     * @return nome do status aprovado
     */
    @Override
    public String getName() {
        return "Aprovado";
    }
    /**
     * Retorna a descrição do status.
     *
     * @return descrição do status aprovado
     */
    @Override
    public String getDescription() {
        return "Solicitação aprovada pela coordenação/supervisão.";
    }
}
