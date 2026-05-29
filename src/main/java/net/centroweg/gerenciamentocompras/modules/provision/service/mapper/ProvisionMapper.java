package net.centroweg.gerenciamentocompras.modules.provision.service.mapper;

import java.util.List;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;

/** 
 * O mapper para serviços.
 * @author gabrielEFagundes
 * @version 1.0
 */
public interface ProvisionMapper {
    /** 
     * Transforma um {@code ProvisionRequest} em uma entidade {@code Provision}
     * @param request A requisição do usuário.
     * @return Provision A entidade {@code Provision} pura.
     */
    Provision toEntity(ProvisionRequest request);

    /** 
     * Transforma um {@code Provision} em uma entidade {@code ProvisionResponse}
     * @param provision A entidade {@code Provision}
     * @return ProvisionResponse O DTO de resposta de {@code Provision}
     */
    ProvisionResponse toResponse(Provision provision);

    /** 
     * Transforma uma lista de {@code Provision} em uma lista de {@code ProvisionResponse}
     * @param provisionList Uma lista de entidades {@code Provision}
     * @return List<ProvisionResponse> Uma lista de {@code ProvisionResponse}
     */
    List<ProvisionResponse> toResponse(List<Provision> provisionList);
}
