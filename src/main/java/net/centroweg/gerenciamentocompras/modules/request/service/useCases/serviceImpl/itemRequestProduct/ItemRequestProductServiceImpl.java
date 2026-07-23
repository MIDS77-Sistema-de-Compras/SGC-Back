package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.ItemRequestProductService;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;

/**
 * Classe de serviço do {@link ItemRequestProduct} que delega cada operação à sua respectiva classe de funcionalidade.
 * Implementa {@link ItemRequestProductService} que segue o princípio de responsabilidade única, onde cada método apenas repassa a chamada para uma classe especializada responsável por uma única operação.
 */
@Service
@RequiredArgsConstructor
public class ItemRequestProductServiceImpl implements ItemRequestProductService {

    /**
     * Componente responsável pela criação de um item de produto da solicitação.
     */
    private final CreateRequestProductService createRequestProductService;

    /**
     * Componente responsável pela listagem de todos os itens de produto da solicitação.
     */
    private final FindAllItemRequestProductService findAllRequestProductService;

    /**
     * Componente responsável pela busca de um item de produto da solicitação pelo seu identificador.
     */
    private final FindByIdItemRequestProductService findRequestProductByIdService;

    /**
     * Componente responsável pela atualização de um item de produto da solicitação.
     */
    private final UpdateItemRequestProductService updateRequestProductService;

    /**
     * Componente responsável por remover um item de produto da solicitação.
     */
    private final DeleteItemRequestProductService deleteRequestProductService;

    /**
     * Cria e persiste um novo item de produto da solicitação no banco de dados.
     * @param request dados do item de produto da solicitação.
     * @return item de produto da solicitação criado.
     */
    @Override
    public ItemRequestProductResponse createRequestProduct(
            ItemRequestProductRequest request
    ) {
        return createRequestProductService.create(request);
    }

    /**
     * Lista todos os itens de produto da solicitação cadastrados.
     * @return lista com todos os itens de produto da solicitação encontrados, caso exista.
     */
    @Override
    public List<ItemRequestProductResponse> findAllRequestProduct() {
        return findAllRequestProductService.findAll();
    }

    /**
     * Busca um item de produto da solicitação no banco de dados pelo ID informado.
     * @param id identificador do item de produto da solicitação.
     * @return item de produto da solicitação encontrado, caso exista.
     */
    @Override
    public ItemRequestProductResponse findRequestProductById(Long id) {
        return findRequestProductByIdService.findById(id);
    }

    /**
     * Atualiza um item de produto da solicitação existente no banco de dados.
     * @param request novos dados do item de produto da solicitação.
     * @param id identificador do item de produto da solicitação.
     * @return item de produto da solicitação já atualizado.
     */
    @Override
    public ItemRequestProductResponse updateRequestProduct(
            ItemRequestProductRequest request,
            Long id
    ) {
        return updateRequestProductService.update(id, request);
    }

    /**
     * Remove um item de produto da solicitação do banco de dados.
     * @param id identificador do item de produto da solicitação.
     */
    @Override
    public void deleteRequestProduct(Long id) {
        deleteRequestProductService.delete(id);}
}
