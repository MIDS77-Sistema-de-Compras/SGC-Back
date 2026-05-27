package net.centroweg.gerenciamentocompras.modules.request.domain.strategy;

import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;

public class InicialStatusImpl implements StatusIntrf {

    @Override
    public String getName() {
        return "Aguardando aprovação";
    }

    @Override
    public String getDescription() {
        return "Solicitação criada e aguardando avaliação da coordenação/supervisão.";
    }

}
