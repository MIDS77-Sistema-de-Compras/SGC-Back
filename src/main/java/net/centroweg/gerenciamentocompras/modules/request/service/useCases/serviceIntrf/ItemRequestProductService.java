package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;

/**
 * Interface de serviço para operações de gerenciamento do {@link ItemRequestProduct}.
 */
public interface ItemRequestProductService {

    /**
     * Cria e persiste um novo item de produto da solicitação no banco de dados.
     * @param request dados do item de produto da solicitação.
     * @return item de produto da solicitação criado.
     */
    ItemRequestProductResponse createRequestProduct(ItemRequestProductRequest request);

    /**
     * Lista todos os itens de produto da solicitação cadastrados no banco de dados.
     * @return lista com todos os itens de produto da solicitação encontrados, caso exista.
     */
    List<ItemRequestProductResponse> findAllRequestProduct();

    /**
     * Busca um item de produto da solicitação no banco de dados pelo ID informado.
     * @param id identificador do item de produto da solicitação.
     * @return item de produto da solicitação encontrado, caso exista.
     */
    ItemRequestProductResponse findRequestProductById(Long id);

    /**
     * Atualiza um item de produto da solicitação existente no banco de dados.
     * @param request novos dados do item de produto da solicitação.
     * @param id identificador do item de produto da solicitação.
     * @return item de produto da solicitação já atualizado.
     */
    ItemRequestProductResponse updateRequestProduct(ItemRequestProductRequest request, Long id);

    /**
     * Remove um item de produto da solicitação do banco de dados.
     * @param id identificador do item de produto da solicitação.
     */
    void deleteRequestProduct(Long id);
}

