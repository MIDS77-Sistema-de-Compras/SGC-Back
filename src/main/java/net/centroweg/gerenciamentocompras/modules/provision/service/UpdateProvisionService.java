package net.centroweg.gerenciamentocompras.modules.provision.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;

/**
 * Classe responsável por atualizar os serviços no banco de dados.
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionRepository
 * @see ProvisionMapper
 */
@Service
@RequiredArgsConstructor
public class UpdateProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapper provisionMapper;

    /**
     * Método responsável por atualizar a entidade {@code Provision} por ID.
     * @param id O ID da {@code Provision} desejada.
     * @param request A requisição do usuário.
     * @return ProvisionResponse A entidade atualizada no banco de dados, como DTO de resposta.
     * @throws ProvisionNotFoundException Se a {@code Provision} não for encontrada.
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
