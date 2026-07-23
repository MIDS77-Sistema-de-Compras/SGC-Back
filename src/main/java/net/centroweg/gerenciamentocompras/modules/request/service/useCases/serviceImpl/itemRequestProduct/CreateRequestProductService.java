package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.itemRequestProduct;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;
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

/**
 * Caso de uso responsável pela criação de um {@link ItemRequestProduct}.
 */
@Service
@RequiredArgsConstructor
public class CreateRequestProductService {

    private final ItemRequestProductRepository itemRequestProductRepository;
    private final RequestRepository requestRepository;
    private final StatusRepository statusRepository;
    private final ItemRequestProductMapper itemRequestProductMapper;
    private final RequestPublicApi requestPublicApi;

    /**
     * Cria e persiste um novo item de produto da solicitação no banco de dados.
     * @param dto dados do item de produto da solicitação.
     * @return item de produto da solicitação criado.
     * @throws RequestNotFoundException caso nenhuma solicitação seja encontrada.
     * @throws ProductNotFoundException caso nenhum produto seja encontrado.
     * @throws MeasurementUnitNotFoundException caso nenhuma unidade de medida seja encontrada.
     * @throws StatusNotFoundException caso nenhum status seja encontrado.
     */
    public ItemRequestProductResponse create(ItemRequestProductRequest dto) {

        Request request = requestRepository.findById(dto.requestId()).orElseThrow(()-> new RequestNotFoundException());

        Product product = requestPublicApi.findProuctByNameIgnoreCase(dto.productName()).orElseThrow(()-> new ProductNotFoundException());

        MeasurementUnit measurementUnit =
                requestPublicApi.findMeasurementByNameIgnoreCase(dto.measurementUnit())
                        .orElseThrow(()-> new MeasurementUnitNotFoundException());

        Status status = statusRepository.findByNameIgnoreCase(dto.statusName())
                .orElseThrow(()-> new StatusNotFoundException());

        ItemRequestProduct itemRequestProduct =
                itemRequestProductMapper.toEntity(
                        dto,
                        request,
                        product,
                        measurementUnit,
                        status
                );

        return itemRequestProductMapper.toResponse(itemRequestProductRepository.save(itemRequestProduct));
    }
}
