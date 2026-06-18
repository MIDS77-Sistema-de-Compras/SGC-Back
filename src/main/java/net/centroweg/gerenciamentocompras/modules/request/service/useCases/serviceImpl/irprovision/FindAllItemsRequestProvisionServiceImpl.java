package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irprovision;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestProvisionItemNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irprovision.ItemRequestProvisionMapper;

@Service
@RequiredArgsConstructor
public class FindAllItemsRequestProvisionServiceImpl {
    
    private final ItemRequestProvisionMapper itemRequestProvisionMapper;
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;

    public List<ItemRequestProvisionResponse> findAll(Long requestId){
        if(requestId != null){
            return itemRequestProvisionMapper.toResponseList(itemRequestProvisionRepository.findAllByRequestId(requestId));
        }

        throw new RequestProvisionItemNotFoundException();
    }

}
