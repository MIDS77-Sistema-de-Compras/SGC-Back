package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irprovision;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irprovision.ItemRequestProvisionMapper;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * Caso de uso responsável pela adição de um {@link ItemRequestProvision}.
 */
@Service
@RequiredArgsConstructor
public class AddItemToRequestProvisionServiceImpl {

    private final ItemRequestProvisionMapper itemRequestProvisionMapper;
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;
    private final RequestRepository requestRepository;
    private final ProvisionRepository provisionRepository;
    private final StatusRepository statusRepository;

    /**
     * Cria e persiste um novo item de provisão da solicitação no banco de dados.
     * @param requestDto dados do item de provisão da solicitação.
     * @return item de provisão da solicitação criado.
     */
    public ItemRequestProvisionResponse addItem(ItemRequestProvisionRequest requestDto){
        Request request = requestRepository.findById(requestDto.requestId())
            .orElseThrow(() -> new RequestNotFoundException());

        Provision provision = provisionRepository.findById(requestDto.provisionId())
            .orElseThrow(() -> new ProvisionNotFoundException());

        Status status = statusRepository.findById(requestDto.statusId())
            .orElseThrow(() -> new StatusNotFoundException());

        return itemRequestProvisionMapper.toResponse(
            itemRequestProvisionRepository.save(
                itemRequestProvisionMapper.toEntity(requestDto, request, provision, status)
            )
        );
    }

}
