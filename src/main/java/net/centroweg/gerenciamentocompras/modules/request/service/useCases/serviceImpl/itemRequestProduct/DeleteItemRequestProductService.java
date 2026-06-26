package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteItemRequestProductService {

    private final ItemRequestProductRepository itemRequestProductRepository;

    public void delete(Long id){
        ItemRequestProduct itemRequestProduct =
                itemRequestProductRepository.findById(id)
                        .orElseThrow(()-> new ItemRequestProductNotFoundException());

        itemRequestProductRepository.deleteById(id);
    }
}
