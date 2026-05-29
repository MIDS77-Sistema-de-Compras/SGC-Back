package net.centroweg.gerenciamentocompras.modules.provision.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapperImpl;

/**
 * Classe responsável por adicionar um serviço ao banco de dados.
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionRepository
 * @see ProvisionMapperImpl
 */
@Service
@RequiredArgsConstructor
public class AddProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapper provisionMapper;

    /**
     * Método responsável por salvar a entidade {@code Provision} fornecida.
     * @param request A requisição do usuário.
     * @return ProvisionResponse A entidade persistida no banco de dados, como DTO de resposta.
     * @see ProvisionMapper#toResponse(Provision)
     * @see ProvisionMapper#toEntity(ProvisionRequest)
     */
    public ProvisionResponse saveNewProvision(ProvisionRequest request){
        return provisionMapper.toResponse(
            provisionRepository.save(
                provisionMapper.toEntity(request)
            )
        );
    }

}
