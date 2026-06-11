package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irp;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestProvisionItemNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irp.ItemRequestProvisionMapper;

@Service
@RequiredArgsConstructor
public class FindItemRequestProvisionByIdServiceImpl {
    
    private final ItemRequestProvisionMapper itemRequestProvisionMapper;
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;

    // possibly implement parameter null type verification
    public ItemRequestProvisionResponse findById(Long requestId, Long itemId){
        return itemRequestProvisionMapper.toResponse(
            itemRequestProvisionRepository.findByIdAndRequestId(itemId, requestId).orElseThrow(() -> new RequestProvisionItemNotFoundException()));
    }

}
