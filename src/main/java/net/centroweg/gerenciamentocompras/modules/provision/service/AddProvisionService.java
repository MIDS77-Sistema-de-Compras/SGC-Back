package net.centroweg.gerenciamentocompras.modules.provision.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapperImpl;

@Service
@RequiredArgsConstructor
public class AddProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapperImpl provisionMapper;

    public ProvisionResponse saveNewProvision(ProvisionRequest request){
        return provisionMapper.toResponse(
            provisionRepository.save(
                provisionMapper.toEntity(request)
            )
        );
    }

}
