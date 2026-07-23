package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct.ItemRequestProductMapper;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável por buscar um {@link ItemRequestProduct} pelo seu identificador.
 */
@Service
@RequiredArgsConstructor
public class FindByIdItemRequestProductService {

    private final ItemRequestProductRepository itemRequestProductRepository;
    private final ItemRequestProductMapper itemRequestProductMapper;

    /**
     * Busca um item de produto da solicitação no banco de dados pelo ID informado.
     * @param id identificador do item de produto da solicitação.
     * @return item de produto da solicitação encontrado, caso exista.
     * @throws ItemRequestProductNotFoundException caso nenhum item de produto da solicitação seja encontrado.
     */
    public ItemRequestProductResponse findById(Long id) {

        ItemRequestProduct itemRequestProduct =
                itemRequestProductRepository.findById(id)
                        .orElseThrow(()-> new ItemRequestProductNotFoundException());

        return itemRequestProductMapper.toResponse(itemRequestProduct);
    }
}