package net.centroweg.gerenciamentocompras.modules.provision.service.mapper;

import java.util.List;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;

/**
 * Interface que isola a camada de apresentação da camada de domínio, evitando exposição pela API.
 */
public interface ProvisionMapper {

    /**
     * Converte um DTO de entrada do serviço em uma entidade serviço.
     * @param request DTO de entrada com os dados.
     * @return dados convertidos para entidade.
     */
    Provision toEntity(ProvisionRequest request);

    /**
     * Converte uma entidade serviço em um DTO de saída do serviço.
     * @param provision entidade com os dados do serviço.
     * @return dados convertidos para DTO de saída.
     */
    ProvisionResponse toResponse(Provision provision);

    /**
     * Converte uma lista de entidades serviço em uma lista de DTOs de saída do serviço.
     * @param provisionList lista de entidades com os dados do serviço.
     * @return dados convertidos para uma lista de DTOs de saída.
     */
    List<ProvisionResponse> toResponse(List<Provision> provisionList);
}
