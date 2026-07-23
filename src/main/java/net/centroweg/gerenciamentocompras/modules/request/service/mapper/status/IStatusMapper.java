package net.centroweg.gerenciamentocompras.modules.request.service.mapper.status;

import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;

/**
 * Interface que isola a camada de apresentação da camada de domínio, evitando exposição pela API.
 */
public interface IStatusMapper {

    /**
     * Converte um DTO de entrada do status em uma entidade status.
     * @param statusRequest dados do status.
     * @return dados convertidos para entidade.
     */
    public Status toEntity (StatusRequest statusRequest);

    /**
     * Converte uma entidade status em um DTO de saída do status.
     * @param status entidade com os dados do status.
     * @return dados convertidos para DTO de saída.
     */
    public StatusResponse toResponse (Status status);

}
