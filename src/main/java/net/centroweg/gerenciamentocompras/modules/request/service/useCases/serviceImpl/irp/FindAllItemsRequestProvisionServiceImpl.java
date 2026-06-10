package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irp;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestProvisionItemNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irp.ItemRequestProvisionMapper;

@Service
@RequiredArgsConstructor
public class FindAllItemsRequestProvisionServiceImpl {
    
    private final ItemRequestProvisionMapper itemRequestProvisionMapper;
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;

    public List<ItemRequestProvisionResponse> findAll(Long requestId){
        if(!requestId.equals(null)){
            return itemRequestProvisionMapper.toResponseList(itemRequestProvisionRepository.findAllByRequestId(requestId));
        }

        throw new RequestProvisionItemNotFoundException();
    }

}
