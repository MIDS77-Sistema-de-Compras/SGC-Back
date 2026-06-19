package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct.ItemRequestProductMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateItemRequestProductService {

    private final ItemRequestProductRepository itemRequestProductRepository;
    private final RequestRepository requestRepository;

    private final RequestPublicApi requestPublicApi;
    private final StatusRepository statusRepository;
    private final ItemRequestProductMapper itemRequestProductMapper;

    public ItemRequestProductResponse update(Long id, ItemRequestProductRequest dto) {

        ItemRequestProduct itemRequestProduct =
                itemRequestProductRepository.findById(id)
                        .orElseThrow(()-> new ItemRequestProductNotFoundException());

        Request request =
                requestRepository.findById(dto.requestId())
                        .orElseThrow(()-> new RequestNotFoundException());

        Product product =
                requestPublicApi.findProuctByNameIgnoreCase(dto.productName())
                        .orElseThrow(()-> new ProductNotFoundException());

        MeasurementUnit measurementUnit =
                requestPublicApi
                        .findMeasurementByNameIgnoreCase(dto.measurementUnit())
                        .orElseThrow(()-> new MeasurementUnitNotFoundException());

        Status status =
                statusRepository.findByNameIgnoreCase(dto.statusName())
                        .orElseThrow(()-> new StatusNotFoundException());

        itemRequestProduct.setRequest(request);
        itemRequestProduct.setProduct(product);
        itemRequestProduct.setMeasurementUnit(measurementUnit);
        itemRequestProduct.setQuantity(dto.quantity());
        itemRequestProduct.setStatus_id(status);
        itemRequestProduct.setAdditionalInformations(dto.additionalInformations());

        return itemRequestProductMapper.toResponse(itemRequestProductRepository.save(itemRequestProduct));
    }
}
