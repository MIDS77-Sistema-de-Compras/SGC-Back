package net.centroweg.gerenciamentocompras.modules.provision.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;

@Service
@RequiredArgsConstructor
public class UpdateProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapper provisionMapper;

    public ProvisionResponse updateProvision(Long id, ProvisionRequest request){
        Provision provision = provisionRepository.findById(id).orElseThrow(() -> 
            new ProvisionNotFoundException("Provision could not be found.")
        );

        provision.setName(request.name());
        provision.setTotalValue(request.totalValue());
        provision.setDescription(request.description());

        return provisionMapper.toResponse(provisionRepository.save(provision));
    }

}
