package net.centroweg.gerenciamentocompras.modules.provision.service.mapper;

import java.util.List;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;

/** The provision mapper.
 * 
 * @author gabrielEFagundes
 * @version 1.0
 */
public interface ProvisionMapper {
    /** Transforms a request of type Provision into an entity Provision
     * 
     * @param request The provision request from an external source
     * @return Provision - The provision entity
     */
    Provision toEntity(ProvisionRequest request);

    /** Transforms an entity of type Provision into a response of Provision
     * 
     * @param provision A provision entity
     * @return ProvisionResponse - The provision response
     */
    ProvisionResponse toResponse(Provision provision);

    /** Transforms a list of entities of type Provision into a list of responses of Provision
     * 
     * @param provisionList A list of entities of type Provision
     * @return List<ProvisionResponse> - The list of provision response
     */
    List<ProvisionResponse> toResponse(List<Provision> provisionList);
}
