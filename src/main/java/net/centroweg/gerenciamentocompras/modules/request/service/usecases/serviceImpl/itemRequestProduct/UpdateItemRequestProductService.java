package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.itemRequestProduct;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.ProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.AcessDeniedException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.StatusNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.StatusRepository;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct.ItemRequestProductMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.AuditInfo;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.Auditable;
import net.centroweg.gerenciamentocompras.shared.persistence.UniqueConstraintViolationDetector;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.springframework.dao.DataIntegrityViolationException;

@Service
@RequiredArgsConstructor
public class UpdateItemRequestProductService {

    private final ItemRequestProductRepository itemRequestProductRepository;
    private final RequestRepository requestRepository;

    private final RequestPublicApi requestPublicApi;
    private final StatusRepository statusRepository;
    private final ItemRequestProductMapper itemRequestProductMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final RequestBusinessRuleValidator requestBusinessRuleValidator;
    private final CurrentUserService currentUserService;

    @Transactional
    @Auditable(action="MUDANÇA_STATUS", targetFromReturn=true)
    public ItemRequestProductResponse update(Long id, 
                @AuditInfo("Novo status: ") ItemRequestProductRequest dto) {

        ItemRequestProduct itemRequestProduct =
                itemRequestProductRepository.findById(id)
                        .orElseThrow(()-> new ItemRequestProductNotFoundException());

        Request request =
                requestRepository.findById(dto.requestId())
                        .orElseThrow(()-> new RequestNotFoundException());

        if (itemRequestProduct.getRequest() == null
                || !Objects.equals(itemRequestProduct.getRequest().getId(), request.getId())) {
            throw new AcessDeniedException();
        }

        User currentUser = currentUserService.getCurrentUser();

        boolean contentChanged = !sameText(itemRequestProduct.getProduct().getName(), dto.productName())
                || (dto.variation() != null && !sameText(itemRequestProduct.getVariation(), dto.variation()))
                || !sameText(itemRequestProduct.getMeasurementUnit().getName(), dto.measurementUnit())
                || !Objects.equals(itemRequestProduct.getQuantity(), dto.quantity())
                || !Objects.equals(itemRequestProduct.getAdditionalInformations(), dto.additionalInformations());
        if (contentChanged) {
            requestBusinessRuleValidator.validateCanEditContent(request, currentUser);
        } else {
            requestBusinessRuleValidator.validateCanEditItems(request, currentUser);
        }

        Product product =
                requestPublicApi.findProuctByNameIgnoreCase(dto.productName())
                        .orElseThrow(()-> new ProductNotFoundException());

        if (itemRequestProductRepository.existsByRequestIdAndProductIdAndIdNot(
                request.getId(),
                product.getId(),
                id
        )) {
            throw new ItemRequestProductAlreadyExistsException();
        }

        MeasurementUnit measurementUnit =
                requestPublicApi
                        .findMeasurementByNameIgnoreCase(dto.measurementUnit())
                        .orElseThrow(()-> new MeasurementUnitNotFoundException());

        Status status =
                statusRepository.findByNameIgnoreCase(dto.statusName())
                        .orElseThrow(()-> new StatusNotFoundException());

        Status previousStatus = itemRequestProduct.getStatus_id();
        boolean statusChanged = previousStatus == null || !Objects.equals(previousStatus.getId(), status.getId());

        itemRequestProduct.setRequest(request);
        itemRequestProduct.setProduct(product);
        if (dto.variation() != null) {
            itemRequestProduct.setVariation(
                    dto.variation().isBlank() ? null : dto.variation().trim()
            );
        }
        itemRequestProduct.setMeasurementUnit(measurementUnit);
        itemRequestProduct.setQuantity(dto.quantity());
        itemRequestProduct.setStatus_id(status);
        itemRequestProduct.setAdditionalInformations(dto.additionalInformations());

        ItemRequestProduct saved;
        try {
            saved = itemRequestProductRepository.save(itemRequestProduct);
            itemRequestProductRepository.flush();
        } catch (DataIntegrityViolationException exception) {
            if (UniqueConstraintViolationDetector.isConstraintViolation(
                    exception,
                    "uq_item_request_product_request_product"
            )) {
                throw new ItemRequestProductAlreadyExistsException();
            }
            throw exception;
        }
        if (statusChanged) {
            eventPublisher.publishEvent(new ItemStatusChangedEvent(
                    request.getId(),
                    saved.getId(),
                    RequestItemType.PRODUCT,
                    product.getName(),
                    product.getCode(),
                    saved.getQuantity(),
                    measurementUnit.getName(),
                    previousStatus != null ? previousStatus.getName() : null,
                    status.getName(),
                    saved.getAdditionalInformations(),
                    LocalDateTime.now()
            ));
        }

        return itemRequestProductMapper.toResponse(saved);
    }

    private boolean sameText(String first, String second) {
        return normalizeOptional(first).equalsIgnoreCase(normalizeOptional(second));
    }

    private String normalizeOptional(String value) {
        return value == null ? "" : value.trim();
    }
}
