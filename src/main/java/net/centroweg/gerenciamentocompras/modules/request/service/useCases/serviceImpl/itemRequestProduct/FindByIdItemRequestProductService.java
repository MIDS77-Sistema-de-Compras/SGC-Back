package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct.ItemRequestProductMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindByIdItemRequestProductService {

    private final ItemRequestProductRepository itemRequestProductRepository;
    private final ItemRequestProductMapper itemRequestProductMapper;

    public ItemRequestProductResponse findById(Long id) {

        ItemRequestProduct itemRequestProduct =
                itemRequestProductRepository.findById(id)
                        .orElseThrow(()-> new ItemRequestProductNotFoundException());

        return itemRequestProductMapper.toResponse(itemRequestProduct);
    }
}