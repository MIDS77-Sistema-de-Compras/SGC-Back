package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
/**
 * Implementação responsável pelo status de solicitação aprovada.
 */
public class ApprovedStatusImpl implements StatusIntrf {
    /**
     * Nome do status.
     * @return nome do status aprovado.
     */
    @Override
    public String getName() {
        return "Aprovado";
    }
    /**
     * Descrição do status.
     * @return descrição do status aprovado.
     */
    @Override
    public String getDescription() {
        return "Solicitação aprovada pela coordenação/supervisão!";
    }
}
