package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irprovision;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestProvisionItemNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irprovision.ItemRequestProvisionMapper;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * Caso de uso responsável por buscar um {@link ItemRequestProvision} pelo seu identificador e identificador da solicitação.
 */
@Service
@RequiredArgsConstructor
public class FindItemRequestProvisionByIdServiceImpl {
    
    private final ItemRequestProvisionMapper itemRequestProvisionMapper;
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;

    /**
     * Busca um item de provisão da solicitação no banco de dados pelo ID e identificador da solicitação informados.
     * @param requestId identificador da solicitação.
     * @param itemId identificador do item de provisão da solicitação.
     * @return item de provisão da solicitação encontrado, caso exista.
     * @throws RequestProvisionItemNotFoundException caso nenhum item de provisão da solicitação seja encontrado.
     */
    public ItemRequestProvisionResponse findById(Long requestId, Long itemId){
        return itemRequestProvisionMapper.toResponse(
            itemRequestProvisionRepository.findByIdAndRequestId(itemId, requestId).orElseThrow(() -> new RequestProvisionItemNotFoundException()));
    }

}
