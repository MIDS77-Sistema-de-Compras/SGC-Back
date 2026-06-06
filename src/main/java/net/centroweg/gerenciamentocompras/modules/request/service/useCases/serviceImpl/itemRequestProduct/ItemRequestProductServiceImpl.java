package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.ItemRequestProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestProductServiceImpl implements ItemRequestProductService {

    private final CreateRequestProductService createRequestProductService;
    private final FindAllItemRequestProductService findAllRequestProductService;
    private final FindByIdItemRequestProductService findRequestProductByIdService;
    private final UpdateItemRequestProductService updateRequestProductService;
    private final DeleteItemRequestProductService deleteRequestProductService;

    @Override
    public ItemRequestProductResponse createRequestProduct(
            ItemRequestProductRequest request
    ) {
        return createRequestProductService.create(request);
    }

    @Override
    public List<ItemRequestProductResponse> findAllRequestProduct() {
        return findAllRequestProductService.findAll();
    }

    @Override
    public ItemRequestProductResponse findRequestProductById(Long id) {
        return findRequestProductByIdService.findById(id);
    }

    @Override
    public ItemRequestProductResponse updateRequestProduct(
            ItemRequestProductRequest request,
            Long id
    ) {
        return updateRequestProductService.update(id, request);
    }

    @Override
    public void deleteRequestProduct(Long id) {
        deleteRequestProductService.delete(id);}
}
