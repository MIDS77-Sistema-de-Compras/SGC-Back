package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irprovision;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.ItemRequestProvisionService;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * Classe de serviço do {@link ItemRequestProvision} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link ItemRequestProvisionService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class IRProvisionServiceImpl implements ItemRequestProvisionService {

    /**
     * Componente responsável pela adição de um item de provisão à solicitação.
     */
    private final AddItemToRequestProvisionServiceImpl addItemToRequestProvision;

    /**
     * Componente responsável pela listagem de todos os itens de provisão da solicitação.
     */
    private final FindAllItemsRequestProvisionServiceImpl findAllItemsRequestProvision;

    /**
     * Componente responsável pela busca de um item de provisão da solicitação pelo seu identificador.
     */
    private final FindItemRequestProvisionByIdServiceImpl findItemRequestProvisionById;

    /**
     * Componente responsável pela atualização de um item de provisão da solicitação.
     */
    private final UpdateItemRequestProvisionServiceImpl updateItemRequestProvision;

    /**
     * Componente responsável por remover um item de provisão da solicitação.
     */
    private final DeleteRequestProvisionItemServiceImpl deleteRequestProvisionItem;

    /**
     * Cria e persiste um novo item de provisão à solicitação no banco de dados.
     * @param request dados do item de provisão da solicitação.
     * @return item de provisão da solicitação criado.
     */
    @Override
    public ItemRequestProvisionResponse addItemToProvisionRequest(ItemRequestProvisionRequest request) {
        return addItemToRequestProvision.addItem(request);
    }

    /**
     * Lista todos os itens de provisão da solicitação cadastrados associados a uma solicitação específica.
     * @param requestId identificador da solicitação.
     * @return lista com todos os itens de provisão da solicitação encontrados, caso exista.
     */
    @Override
    public List<ItemRequestProvisionResponse> findAllProvisionRequestItems(Long requestId) {
        return findAllItemsRequestProvision.findAll(requestId);
    }

    /**
     * Busca um item de provisão da solicitação no banco de dados pelo ID e identificador da solicitação informados.
     * @param requestId identificador da solicitação.
     * @param itemId identificador do item de provisão da solicitação.
     * @return item de provisão da solicitação encontrado, caso exista.
     */
    @Override
    public ItemRequestProvisionResponse findProvisionRequestItemById(Long requestId, Long itemId) {
        return findItemRequestProvisionById.findById(requestId, itemId);
    }

    /**
     * Atualiza um item de provisão da solicitação existente no banco de dados.
     * @param itemId identificador do item de provisão da solicitação.
     * @param request novos dados do item de provisão da solicitação.
     * @return item de provisão da solicitação já atualizado.
     */
    @Override
    public ItemRequestProvisionResponse updateItemFromProvisionRequest(Long itemId, ItemRequestProvisionRequest request) {
        return updateItemRequestProvision.updateItem(itemId, request);
    }

    /**
     * Remove um item de provisão da solicitação do banco de dados.
     * @param itemId identificador do item de provisão da solicitação.
     */
    @Override
    public void deleteItemFromProvisionRequest(Long itemId) {
        deleteRequestProvisionItem.deleteItem(itemId);
    }

}
