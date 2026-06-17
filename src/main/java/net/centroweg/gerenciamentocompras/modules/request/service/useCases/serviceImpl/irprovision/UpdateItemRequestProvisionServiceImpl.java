package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irprovision;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence.ProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestProvisionItemNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irprovision.ItemRequestProvisionMapper;

@Service
@RequiredArgsConstructor
public class UpdateItemRequestProvisionServiceImpl {
    
    private final ItemRequestProvisionMapper itemRequestProvisionMapper;
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;

    private final RequestRepository requestRepository;
    private final ProvisionRepository provisionRepository;
    private final StatusRepository statusRepository;

    public ItemRequestProvisionResponse updateItem(Long itemId, ItemRequestProvisionRequest requestDto){
        ItemRequestProvision item = itemRequestProvisionRepository.findById(itemId)
            .orElseThrow(() -> new RequestProvisionItemNotFoundException());

        Request request = requestRepository.findById(requestDto.requestId())
            .orElseThrow(() -> new RequestNotFoundException());

        Provision provision = provisionRepository.findById(requestDto.provisionId())
            .orElseThrow(() -> new ProvisionNotFoundException());

        Status status = statusRepository.findById(requestDto.statusId())
            .orElseThrow(() -> new StatusNotFoundException());

        item.setRequest(request);
        item.setProvision(provision);
        item.setStatus(status);

        if(!requestDto.additionalInformation().isBlank()){
            item.setAdditionalInformation(requestDto.additionalInformation());
        }

        // also have to update the dependencies
        request.getItemRequestProvisions().add(item);
        provision.getItemRequestProvisions().add(item);
        status.getItemRequestProvisions().add(item);

        return itemRequestProvisionMapper.toResponse(itemRequestProvisionRepository.save(item));
    }

}
