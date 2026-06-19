package net.centroweg.gerenciamentocompras.modules.provision.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;

/**
 * Classe responsável por buscar os serviços no banco de dados.
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionRepository
 * @see ProvisionMapper
 */
@Service
@RequiredArgsConstructor
public class GetProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapper provisionMapper;

    /**
     * Método responsável por buscar todas as entidades {@code Provision}.
     * @return {@code List<ProvisionResponse>} Uma lista de {@code ProvisionResponse}.
     * @see ProvisionMapper#toResponse(List)
     */
    public List<ProvisionResponse> getAllProvisions(){
        return provisionMapper.toResponse(provisionRepository.findAll());
    }

    /**
     * Método responsável por buscar uma entidade {@code Provision} por ID.
     * @param id O ID da {@code Provision} desejada.
     * @return ProvisionResponse A entidade encontrada no banco de dados.
     * @throws ProvisionNotFoundException Se a {@code Provision} não for encontrada.
     * @see ProvisionMapper#toResponse(Provision)
     */
    public ProvisionResponse getProvisionById(Long id){
        return provisionMapper.toResponse(
            provisionRepository.findById(id).orElseThrow(() -> 
                new ProvisionNotFoundException()
            )
        );
    }

}
