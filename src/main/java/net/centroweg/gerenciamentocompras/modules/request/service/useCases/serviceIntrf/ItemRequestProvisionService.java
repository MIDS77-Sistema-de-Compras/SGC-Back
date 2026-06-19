package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;

import java.util.List;
import java.util.Optional;

import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;

/** Métodos para service de ItemRequestProvision
 * 
 * @author gabrielEFagundes
 * @version 1.0
 * @see {@code ItemRequestProvision}
 */
public interface ItemRequestProvisionService {

    /** Adiciona um item à solicitação de serviço.
     * 
     * @param request DTO de request do usuário.
     * @return DTO de resposta ao usuário.
     */
    ItemRequestProvisionResponse addItemToProvisionRequest(ItemRequestProvisionRequest request);

    /** Encontra todos os itens da solicitação específica.
     * 
     * Vale notar que esse método não verifica se a solicitação é de serviço ou de produto, isso fica à implementar.
     * 
     * @param requestId ID da solicitação
     * @return Lista de DTOs de resposta ao usuário.
     */
    List<ItemRequestProvisionResponse> findAllProvisionRequestItems(Long requestId);

    /** Encontra o item específico da solicitação de serviço.
     * 
     * @param id ID do item da solicitação
     * @return DTO de resposta ao usuário.
     */
    ItemRequestProvisionResponse findProvisionRequestItemById(Long requestId, Long itemId);

    /** Atualiza o item de solicitação de serviço baseado no seu ID
     * 
     * @param itemId ID do item da solicitação
     * @param request DTO de requesto do usuário
     * @return DTO de resposta ao usuário
     */
    ItemRequestProvisionResponse updateItemFromProvisionRequest(Long itemId, ItemRequestProvisionRequest request);

    /** Remove o item da solicitação de serviço.
     * 
     * @param itemId ID do item da solicitação
     */
    void deleteItemFromProvisionRequest(Long itemId);
    
}
