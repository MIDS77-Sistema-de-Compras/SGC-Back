package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.itemRequestProduct;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
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
public class CreateRequestProductService {

    private final ItemRequestProductRepository itemRequestProductRepository;
    private final RequestRepository requestRepository;
    private final StatusRepository statusRepository;
    private final ItemRequestProductMapper itemRequestProductMapper;
    private final RequestPublicApi requestPublicApi;

    public ItemRequestProductResponse create(ItemRequestProductRequest dto) {

        Request request = requestRepository.findById(dto.requestId())
                .orElseThrow(RequestNotFoundException::new);

        Product product = requestPublicApi.findProuctByNameIgnoreCase(dto.productName())
                .orElseThrow(ProductNotFoundException::new);

        MeasurementUnit measurementUnit = requestPublicApi.findMeasurementByNameIgnoreCase(dto.measurementUnit())
                .orElseThrow(MeasurementUnitNotFoundException::new);

        Status status = statusRepository.findByNameIgnoreCase(dto.statusName())
                .orElseThrow(StatusNotFoundException::new);

        ItemRequestProduct itemRequestProduct = itemRequestProductMapper.toEntity(
                dto,
                request,
                product,
                measurementUnit,
                status
        );

        return itemRequestProductMapper.toResponse(itemRequestProductRepository.save(itemRequestProduct));
    }
}