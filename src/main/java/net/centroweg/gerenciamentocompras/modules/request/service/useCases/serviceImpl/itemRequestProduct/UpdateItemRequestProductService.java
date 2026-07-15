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
import net.centroweg.gerenciamentocompras.modules.request.service.event.ItemStatusChangedEvent;
import net.centroweg.gerenciamentocompras.modules.request.service.event.RequestItemType;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.service.mapper.itemRequestProduct.ItemRequestProductMapper;
import net.centroweg.gerenciamentocompras.modules.request.service.validator.RequestBusinessRuleValidator;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import net.centroweg.gerenciamentocompras.shared.security.CurrentUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateItemRequestProductService {

    private static final String APPROVED_STATUS = "Aprovado";
    private static final String RECUSED_STATUS = "Recusado";
    private static final String PARTIALLY_APPROVED_STATUS = "Parcialmente aprovada";

    private final ItemRequestProductRepository itemRequestProductRepository;
    private final RequestRepository requestRepository;

    private final RequestPublicApi requestPublicApi;
    private final StatusRepository statusRepository;
    private final ItemRequestProductMapper itemRequestProductMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final RequestBusinessRuleValidator requestBusinessRuleValidator;
    private final CurrentUserService currentUserService;

    @Transactional
    public ItemRequestProductResponse update(Long id, ItemRequestProductRequest dto) {

        ItemRequestProduct itemRequestProduct =
                itemRequestProductRepository.findById(id)
                        .orElseThrow(()-> new ItemRequestProductNotFoundException());

        Request request =
                requestRepository.findById(dto.requestId())
                        .orElseThrow(()-> new RequestNotFoundException());

        User currentUser = currentUserService.getCurrentUser();
        requestBusinessRuleValidator.validateCanEditItems(request, currentUser);

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

        Status previousStatus = itemRequestProduct.getStatus_id();
        boolean statusChanged = previousStatus == null || !Objects.equals(previousStatus.getId(), status.getId());

        itemRequestProduct.setRequest(request);
        itemRequestProduct.setProduct(product);
        itemRequestProduct.setMeasurementUnit(measurementUnit);
        itemRequestProduct.setVariation(dto.variation());
        itemRequestProduct.setQuantity(dto.quantity());
        itemRequestProduct.setStatus_id(status);
        itemRequestProduct.setAdditionalInformations(dto.additionalInformations());

        ItemRequestProduct saved = itemRequestProductRepository.save(itemRequestProduct);
        if (statusChanged) {
            recalculateRequestStatus(request);

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

    /**
     * Recalcula o status geral da solicitação a partir da decisão (aprovado/recusado)
     * de cada item de produto e serviço. Enquanto houver item sem decisão, o status
     * geral não é alterado. Quando todos os itens tiverem decisão, a solicitação vira
     * "Aprovado" (todos aprovados), "Recusado" (todos recusados) ou
     * "Parcialmente aprovada" (decisão mista).
     */
    private void recalculateRequestStatus(Request request) {
        List<String> itemStatusNames = new java.util.ArrayList<>();

        request.getItemRequestProducts().forEach(item -> {
            if (item.getStatus_id() != null) {
                itemStatusNames.add(normalizeStatusName(item.getStatus_id().getName()));
            }
        });

        request.getItemRequestProvisions().forEach(item -> {
            if (item.getStatus() != null) {
                itemStatusNames.add(normalizeStatusName(item.getStatus().getName()));
            }
        });

        if (itemStatusNames.isEmpty()) {
            return;
        }

        boolean hasPendingItem = itemStatusNames.stream()
                .anyMatch(name -> !name.equals(normalizeStatusName(APPROVED_STATUS))
                        && !name.equals(normalizeStatusName(RECUSED_STATUS)));

        if (hasPendingItem) {
            return;
        }

        boolean allApproved = itemStatusNames.stream()
                .allMatch(name -> name.equals(normalizeStatusName(APPROVED_STATUS)));
        boolean allRecused = itemStatusNames.stream()
                .allMatch(name -> name.equals(normalizeStatusName(RECUSED_STATUS)));

        String newStatusName = allApproved
                ? APPROVED_STATUS
                : allRecused ? RECUSED_STATUS : PARTIALLY_APPROVED_STATUS;

        Status newStatus = statusRepository.findByNameIgnoreCase(newStatusName)
                .orElseThrow(StatusNotFoundException::new);

        if (request.getStatus() == null || !Objects.equals(request.getStatus().getId(), newStatus.getId())) {
            request.setStatus(newStatus);
            requestRepository.save(request);
        }
    }

    private String normalizeStatusName(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
