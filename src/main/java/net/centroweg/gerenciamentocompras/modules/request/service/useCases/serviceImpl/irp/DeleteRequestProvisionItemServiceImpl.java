package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irp;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestProvisionItemNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.ItemRequestProvisionRepository;

@Service
@RequiredArgsConstructor
public class DeleteRequestProvisionItemServiceImpl {
    
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;

    public void deleteItem(Long itemId){
        if(!itemRequestProvisionRepository.existsById(itemId)){
            throw new RequestProvisionItemNotFoundException();
        }

        itemRequestProvisionRepository.deleteById(itemId);
    }

}
