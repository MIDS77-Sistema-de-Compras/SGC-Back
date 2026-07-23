package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irprovision;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestProvisionItemNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * Caso de uso responsável por remover um {@link ItemRequestProvision}.
 */
@Service
@RequiredArgsConstructor
public class DeleteRequestProvisionItemServiceImpl {
    
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;

    /**
     * Remove um item de provisão da solicitação do banco de dados.
     * @param itemId identificador do item de provisão da solicitação.
     * @throws RequestProvisionItemNotFoundException caso nenhum item de provisão da solicitação seja encontrado.
     */
    public void deleteItem(Long itemId){
        if(!itemRequestProvisionRepository.existsById(itemId)){
            throw new RequestProvisionItemNotFoundException();
        }

        itemRequestProvisionRepository.deleteById(itemId);
    }

}
