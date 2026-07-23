package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;

import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * Interface de serviço para operações de gerenciamento do {@link ItemRequestProvision}.
 */
public interface ItemRequestProvisionService {

    /**
     * Adiciona e persiste um novo item de provisão à solicitação no banco de dados.
     * @param request dados do item de provisão da solicitação.
     * @return item de provisão da solicitação criado.
     */
    ItemRequestProvisionResponse addItemToProvisionRequest(ItemRequestProvisionRequest request);

    /**
     * Lista todos os itens de provisão da solicitação cadastrados no banco de dados associados a uma solicitação específica.
     * @param requestId identificador da solicitação.
     * @return lista com todos os itens de provisão da solicitação encontrados, caso exista.
     */
    List<ItemRequestProvisionResponse> findAllProvisionRequestItems(Long requestId);

    /**
     * Busca um item de provisão da solicitação no banco de dados pelo ID e identificador da solicitação informados.
     * @param requestId identificador da solicitação.
     * @param itemId identificador do item de provisão da solicitação.
     * @return item de provisão da solicitação encontrado, caso exista.
     */
    ItemRequestProvisionResponse findProvisionRequestItemById(Long requestId, Long itemId);

    /**
     * Atualiza um item de provisão da solicitação existente no banco de dados.
     * @param itemId identificador do item de provisão da solicitação.
     * @param request novos dados do item de provisão da solicitação.
     * @return item de provisão da solicitação já atualizado.
     */
    ItemRequestProvisionResponse updateItemFromProvisionRequest(Long itemId, ItemRequestProvisionRequest request);

    /**
     * Remove um item de provisão da solicitação do banco de dados.
     * @param itemId identificador do item de provisão da solicitação.
     */
    void deleteItemFromProvisionRequest(Long itemId);
    
}
