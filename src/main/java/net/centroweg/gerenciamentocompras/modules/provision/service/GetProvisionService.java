package net.centroweg.gerenciamentocompras.modules.provision.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;

@Service
@RequiredArgsConstructor
public class GetProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapper provisionMapper;

    public List<ProvisionResponse> getAllProvisions(){
        return provisionMapper.toResponse(provisionRepository.findAll());
    }

    public ProvisionResponse getProvisionById(Long id){
        return provisionMapper.toResponse(
            provisionRepository.findById(id).orElseThrow(() -> 
                new ProvisionNotFoundException("Provision could not be found.")
            )
        );
    }

}
