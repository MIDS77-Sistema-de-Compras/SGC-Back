package net.centroweg.gerenciamentocompras.modules.provision.service.mapper;

import org.springframework.stereotype.Component;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;

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

}
