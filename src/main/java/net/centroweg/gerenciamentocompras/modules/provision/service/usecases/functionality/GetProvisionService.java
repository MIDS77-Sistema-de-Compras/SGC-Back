package net.centroweg.gerenciamentocompras.modules.provision.service.usecases.functionality;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;

/**
 * Caso de uso responsável por buscar ou listar um {@link Provision} pelo seu identificador ou nome.
 */
@Service
@RequiredArgsConstructor
public class GetProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapper provisionMapper;

    /**
     * Lista todos os serviços cadastrados no banco de dados.
     * @return lista com todos os serviços encontrados, caso exista.
     */
    public List<ProvisionResponse> getAllProvisions(){
        return provisionMapper.toResponse(provisionRepository.findAll());
    }

    /**
     * Busca um serviço no banco de dados pelo ID informado.
     * @param id identificador do serviço.
     * @return serviço encontrado, caso exista.
     * @throws ProvisionNotFoundException caso nenhum serviço seja encontrado.
     */
    public ProvisionResponse getProvisionById(Long id){
        return provisionMapper.toResponse(
            provisionRepository.findById(id).orElseThrow(() -> 
                new ProvisionNotFoundException()
            )
        );
    }

}
