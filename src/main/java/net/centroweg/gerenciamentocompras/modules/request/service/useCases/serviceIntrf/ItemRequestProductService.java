package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf;

import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;

import java.util.List;

public interface ItemRequestProductService {

    ItemRequestProductResponse createRequestProduct(
            ItemRequestProductRequest request
    );

    List<ItemRequestProductResponse> findAllRequestProduct();

    ItemRequestProductResponse findRequestProductById(Long id);

    ItemRequestProductResponse updateRequestProduct(
            ItemRequestProductRequest request,
            Long id
    );

    void deleteRequestProduct(Long id);
}

