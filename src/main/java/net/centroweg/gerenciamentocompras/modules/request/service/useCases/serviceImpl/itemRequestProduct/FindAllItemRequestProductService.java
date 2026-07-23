package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct.ItemRequestProductMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * Caso de uso responsável por listar um {@link ItemRequestProvision}.
 */
@Service
@RequiredArgsConstructor
public class FindAllItemRequestProductService {

    private final ItemRequestProductRepository itemRequestProductRepository;
    private final ItemRequestProductMapper itemRequestProductMapper;

    /**
     * Lista todos os itens de produto da solicitação cadastrados no banco de dados.
     * @return lista com todos os itens de produto da solicitação encontrados, caso exista.
     */
    public List<ItemRequestProductResponse> findAll() {
        return itemRequestProductRepository.findAll()
                .stream()
                .map(itemRequestProductMapper::toResponse)
                .toList();
    }
}