package net.centroweg.gerenciamentocompras.modules.provision.service.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;

/**
 * Classe responsável por implementar os métodos da interface {@code ProvisionMapper}
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionMapper
 */
@Component
public class ProvisionMapperImpl implements ProvisionMapper {

    @Override
    public Provision toEntity(ProvisionRequest request) {
        return new Provision(request.name(), request.totalValue(), request.description());
    }

    @Override
    public ProvisionResponse toResponse(Provision provision) {
        return new ProvisionResponse(provision.getId(), provision.getName(), provision.getTotalValue(), provision.getDescription());
    }

    @Override
    public List<ProvisionResponse> toResponse(List<Provision> provisionList) {
        return provisionList.stream()
            .map(provision -> new ProvisionMapperImpl().toResponse(provision))
            .toList();
    }

}
