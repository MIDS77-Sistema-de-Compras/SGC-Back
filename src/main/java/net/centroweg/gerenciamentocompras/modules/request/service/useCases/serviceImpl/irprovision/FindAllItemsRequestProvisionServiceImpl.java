package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irprovision;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestProvisionItemNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.irprovision.ItemRequestProvisionMapper;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * Caso de uso responsável por listar um {@link ItemRequestProvision} pelo identificador da solicitação.
 */
@Service
@RequiredArgsConstructor
public class FindAllItemsRequestProvisionServiceImpl {
    
    private final ItemRequestProvisionMapper itemRequestProvisionMapper;
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;

    /**
     * Lista todos os itens de provisão da solicitação cadastrados no banco de dados associados a uma solicitação específica.
     * @param requestId identificador da solicitação.
     * @return lista com todos os itens de provisão da solicitação encontrados, caso exista.
     * @throws RequestProvisionItemNotFoundException caso nenhum item de provisão da solicitação seja encontrado.
     */
    public List<ItemRequestProvisionResponse> findAll(Long requestId){
        if(requestId != null){
            return itemRequestProvisionMapper.toResponseList(itemRequestProvisionRepository.findAllByRequestId(requestId));
        }

        throw new RequestProvisionItemNotFoundException();
    }

}
