package net.centroweg.gerenciamentocompras.modules.provision.service.usecases.functionality;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.mapper.ProvisionMapper;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;

/**
 * Caso de uso responsável pela criação de um {@link Provision}.
 */
@Service
@RequiredArgsConstructor
public class AddProvisionService {
    
    private final ProvisionRepository provisionRepository;
    private final ProvisionMapper provisionMapper;

    /**
     * Cria e persiste um novo serviço no banco de dados.
     * @param request dados do serviço.
     * @return serviço criado.
     */
    public ProvisionResponse saveNewProvision(ProvisionRequest request){
        return provisionMapper.toResponse(
            provisionRepository.save(
                provisionMapper.toEntity(request)
            )
        );
    }

}
