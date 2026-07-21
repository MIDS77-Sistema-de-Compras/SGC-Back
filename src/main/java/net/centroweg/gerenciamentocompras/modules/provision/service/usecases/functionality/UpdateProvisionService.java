package net.centroweg.gerenciamentocompras.modules.provision.service.usecases.functionality;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;

/**
 * Caso de uso responsável pela atualização de um {@link Provision}.
 */
@Service
@RequiredArgsConstructor
public class UpdateProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapper provisionMapper;

    /**
     * Atualiza um serviço existente no banco de dados.
     * @param id identificador do serviço.
     * @param request novos dados do serviço.
     * @return serviço já atualizado.
     * @throws ProvisionNotFoundException caso nenhum serviço seja encontrado.
     */
    public ProvisionResponse updateProvision(Long id, ProvisionRequest request){
        Provision provision = provisionRepository.findById(id).orElseThrow(() -> 
            new ProvisionNotFoundException()
        );

        provision.setName(request.name());
        provision.setTotalValue(request.totalValue());
        provision.setDescription(request.description());

        return provisionMapper.toResponse(provisionRepository.save(provision));
    }

}
