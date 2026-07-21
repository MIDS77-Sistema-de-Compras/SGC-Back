package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

/**
 * Implementação responsável pelo status inicial da solicitação.
 */
public class InicialStatusImpl implements StatusIntrf {

    /**
     * Nome do status.
     * @return nome do status inicial.
     */
    @Override
    public String getName() {
        return "Aguardando aprovação";
    }

    /**
     * Descrição do status.
     * @return descrição do status inicial.
     */
    @Override
    public String getDescription() {
        return "Solicitação criada e aguardando avaliação da coordenação/supervisão!";
    }

}
