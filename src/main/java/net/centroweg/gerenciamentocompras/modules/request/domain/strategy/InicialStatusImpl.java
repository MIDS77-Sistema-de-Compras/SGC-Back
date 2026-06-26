package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
/**
 * Implementação responsável pelo status inicial da solicitação.
 *
 * <p>Representa que a solicitação foi criada e está
 * aguardando aprovação da coordenação ou supervisão.</p>
 *
 * @author André
 * @since 1.0
 */
public class InicialStatusImpl implements StatusIntrf {
    /**
     * Retorna o nome do status.
     *
     * @return nome do status inicial
     */
    @Override
    public String getName() {
        return "Aguardando aprovação";
    }
    /**
     * Retorna a descrição do status.
     *
     * @return descrição do status inicial
     */
    @Override
    public String getDescription() {
        return "Solicitação criada e aguardando avaliação da coordenação/supervisão.";
    }

}
