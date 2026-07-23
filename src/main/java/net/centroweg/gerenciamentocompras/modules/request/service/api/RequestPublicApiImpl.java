package net.centroweg.gerenciamentocompras.modules.request.service.api;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.service.api.ProductPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.RequestNotFoundException;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProductRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.ItemRequestProvisionRepository;
import net.centroweg.gerenciamentocompras.modules.request.infrastructure.persistence.repository.RequestRepository;
import net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request.ConcludeRequestServiceImpl;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestNotificationRecipient;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestStatusNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestEmailNotificationData;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestProductEmailData;
import net.centroweg.gerenciamentocompras.modules.request.service.api.dto.RequestProvisionEmailData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RequestPublicApiImpl implements RequestPublicApi {

    private final ProductPublicApi productPublicApi;
    private final RequestRepository requestRepository;
    private final ItemRequestProductRepository itemRequestProductRepository;
    private final ItemRequestProvisionRepository itemRequestProvisionRepository;
    private final ConcludeRequestServiceImpl concludeRequestServiceImpl;

    @Override
    public void concludeRequest(Long requestId, String statusName) {
        concludeRequestServiceImpl.concludeRequest(requestId, statusName);
    }

    @Override
    public Optional<Product> findProuctByNameIgnoreCase(String name) {
        return productPublicApi.findByNameIgnoreCase(name);
    }

    @Override
    public Optional<MeasurementUnit> findMeasurementByNameIgnoreCase(String nameOrAbbreviation) {
        return productPublicApi.findMeasurementByNameOrAbbreviation(nameOrAbbreviation);
    }

    @Override
    public Product createProduct(CreateProductRequest request) {
        return productPublicApi.createProduct(request);
    }

    @Override
    public Optional<Request> findRequestById(Long requestId) {
        return requestRepository.findById(requestId);
    }

    @Override
    public List<ItemRequestProduct> findItemProductsByIds(Collection<Long> ids) {
        return itemRequestProductRepository.findAllById(ids);
    }

    @Override
    public List<ItemRequestProvision> findItemProvisionsByIds(Collection<Long> ids) {
        return itemRequestProvisionRepository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public RequestNotificationData findNotificationDataById(Long requestId) {
        Request request = requestRepository.findWithRequestersById(requestId)
                .orElseThrow(RequestNotFoundException::new);

        List<RequestNotificationRecipient> recipients = request.getCreatedByUsers() == null
                ? List.of()
                : request.getCreatedByUsers().stream()
                        .filter(java.util.Objects::nonNull)
                        .map(user -> new RequestNotificationRecipient(
                                user.getId(),
                                user.getName(),
                                user.getEmail()
                        ))
                        .toList();

        return new RequestNotificationData(request.getId(), recipients);
    }

    @Override
    @Transactional(readOnly = true)
    public RequestStatusNotificationData findStatusNotificationDataById(Long requestId) {
        Request request = requestRepository.findForStatusNotificationById(requestId)
                .orElseThrow(RequestNotFoundException::new);
        return new RequestStatusNotificationData(
                request.getId(),
                request.getCrBranch().getCr().getName(),
                request.getCrBranch().getCr().getCode(),
                request.getCrBranch().getBranch().getName(),
                request.getStatus().getName(),
                request.getRequestDate(),
                toRecipients(request)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RequestEmailNotificationData findEmailNotificationDataById(Long requestId) {
        Request request = requestRepository.findForEmailNotificationById(requestId)
                .orElseThrow(RequestNotFoundException::new);
        return new RequestEmailNotificationData(
                request.getId(),
                request.getCrBranch().getCr().getName(),
                request.getCrBranch().getCr().getCode(),
                request.getCrBranch().getBranch().getName(),
                request.getStatus().getName(),
                request.getCreatedByUsers().isEmpty() ? null : request.getCreatedByUsers().getFirst().getName(),
                request.getRequestDate(),
                request.getItemRequestProducts().stream()
                        .map(item -> new RequestProductEmailData(
                                item.getProduct().getName(),
                                item.getProduct().getCode(),
                                item.getQuantity(),
                                item.getMeasurementUnit() == null ? null : item.getMeasurementUnit().getName(),
                                item.getAdditionalInformations()
                        )).toList(),
                request.getItemRequestProvisions().stream()
                        .map(item -> new RequestProvisionEmailData(
                                item.getProvision().getName(),
                                item.getProvision().getTotalValue(),
                                item.getProvision().getDescription(),
                                item.getAdditionalInformation()
                        )).toList()
        );
    }

    private List<RequestNotificationRecipient> toRecipients(Request request) {
        return request.getCreatedByUsers() == null ? List.of() : request.getCreatedByUsers().stream()
                .filter(java.util.Objects::nonNull)
                .map(user -> new RequestNotificationRecipient(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }
}
