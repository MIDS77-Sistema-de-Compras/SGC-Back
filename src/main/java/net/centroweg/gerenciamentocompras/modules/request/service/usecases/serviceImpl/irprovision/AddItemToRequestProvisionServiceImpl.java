package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.irprovision;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.service.api.ProvisionPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProvisionAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irprovision.ItemRequestProvisionMapper;

@Service
@RequiredArgsConstructor
public class AddItemToRequestProvisionServiceImpl {
    
    private final ItemRequestProvisionMapper itemRequestProvisionMapper;
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;

    private final RequestRepository requestRepository;
    private final ProvisionPublicApi provisionPublicApi;
    private final StatusRepository statusRepository;

    public ItemRequestProvisionResponse addItem(ItemRequestProvisionRequest requestDto){
        Request request = requestRepository.findById(requestDto.requestId())
            .orElseThrow(() -> new RequestNotFoundException());

        Provision provision = provisionPublicApi.findById(requestDto.provisionId())
            .orElseThrow(() -> new ProvisionNotFoundException());

        if (itemRequestProvisionRepository.existsByRequestIdAndProvisionId(request.getId(), provision.getId())) {
            throw new ItemRequestProvisionAlreadyExistsException();
        }

        Status status = statusRepository.findById(requestDto.statusId())
            .orElseThrow(() -> new StatusNotFoundException());

        return itemRequestProvisionMapper.toResponse(
            itemRequestProvisionRepository.save(
                itemRequestProvisionMapper.toEntity(requestDto, request, provision, status)
            )
        );
    }

}
