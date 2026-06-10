package net.centroweg.gerenciamentocompras.modules.provision.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;

@Service
@RequiredArgsConstructor
public class DeleteProvisionService {
    
    private final ProvisionRepository provisionRepository;

    public void  deleteProvisionById(Long id){
        if(!provisionRepository.existsById(id)){
            throw new ProvisionNotFoundException();
        }

        provisionRepository.deleteById(id);
    }

}
