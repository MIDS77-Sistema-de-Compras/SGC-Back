package net.centroweg.gerenciamentocompras.modules.request.service.usecases.serviceImpl.request;

import java.util.List;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.Product;
import net.centroweg.gerenciamentocompras.modules.product.domain.exception.MeasurementUnitNotFoundException;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.InsufficientProvisionDataException;
import net.centroweg.gerenciamentocompras.modules.provision.domain.exception.ProvisionNotFoundException;
import net.centroweg.gerenciamentocompras.modules.provision.service.api.ProvisionPublicApi;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Status;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProductAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.request.domain.exception.ItemRequestProvisionAlreadyExistsException;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestProductItemRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestProvisionItemRequest;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;

@Component
@RequiredArgsConstructor
public class RequestItemsAssembler {

    private static final String REQUEST_PRODUCT_TYPE = "Solicitacao";
    private static final double REQUEST_PRODUCT_DEFAULT_PRICE = 0.0;

    private final RequestPublicApi requestPublicApi;
    private final ProvisionPublicApi provisionPublicApi;

    public void replaceItems(
            Request request,
            Status status,
            List<RequestProductItemRequest> products,
            List<RequestProvisionItemRequest> provisions
    ) {
        request.getItemRequestProducts().clear();
        request.getItemRequestProvisions().clear();
        addProductItems(products, request, status);
        addProvisionItems(provisions, request, status);
    }

    public void addItems(
            Request request,
            Status status,
            List<RequestProductItemRequest> products,
            List<RequestProvisionItemRequest> provisions
    ) {
        addProductItems(products, request, status);
        addProvisionItems(provisions, request, status);
    }

    private void addProductItems(
            List<RequestProductItemRequest> products,
            Request request,
            Status status
    ) {
        if (products == null) return;

        // Rejeita o mesmo produto informado mais de uma vez na mesma solicitação.
        Set<String> seenProductNames = new HashSet<>();

        for (RequestProductItemRequest productRequest : products) {
            String productName = normalizeName(productRequest.productName());
            if (!seenProductNames.add(normalizeNameKey(productName))) {
                throw new ItemRequestProductAlreadyExistsException();
            }

            Product product = findOrCreateProduct(productName, productRequest);
            MeasurementUnit measurementUnit = requestPublicApi
                    .findMeasurementByNameIgnoreCase(productRequest.measurementUnit())
                    .orElseThrow(MeasurementUnitNotFoundException::new);

            ItemRequestProduct item = new ItemRequestProduct();
            item.setRequest(request);
            item.setProduct(product);
            item.setVariation(normalizeVariation(productRequest.variation()));
            item.setMeasurementUnit(measurementUnit);
            item.setQuantity(productRequest.quantity());
            item.setStatus_id(status);
            item.setAdditionalInformations(productRequest.additionalInformations());
            request.getItemRequestProducts().add(item);
        }
    }

    // Reaproveita o produto existente pelo nome (entre solicitações) ou cria um novo.
    private Product findOrCreateProduct(String productName, RequestProductItemRequest productRequest) {
        return requestPublicApi.findProuctByNameIgnoreCase(productName)
                .orElseGet(() -> requestPublicApi.createProduct(new CreateProductRequest(
                        productName,
                        productRequest.additionalInformations(),
                        REQUEST_PRODUCT_DEFAULT_PRICE,
                        REQUEST_PRODUCT_TYPE,
                        "REQ-" + UUID.randomUUID().toString().replace("-", "")
                )));
    }

    private void addProvisionItems(
            List<RequestProvisionItemRequest> provisions,
            Request request,
            Status status
    ) {
        if (provisions == null) return;

        // Rejeita o mesmo serviço informado mais de uma vez na mesma solicitação
        // (por provisionId, para serviços existentes, ou por nome, para novos).
        Set<Long> seenProvisionIds = new HashSet<>();
        Set<String> seenNewProvisionNames = new HashSet<>();

        for (RequestProvisionItemRequest provisionRequest : provisions) {
            Provision provision = resolveProvision(provisionRequest, seenProvisionIds, seenNewProvisionNames);
            ItemRequestProvision item = new ItemRequestProvision(
                    request,
                    provision,
                    status,
                    provisionRequest.additionalInformation()
            );
            request.getItemRequestProvisions().add(item);
        }
    }

    private Provision resolveProvision(
            RequestProvisionItemRequest request,
            Set<Long> seenProvisionIds,
            Set<String> seenNewProvisionNames
    ) {
        // Quando um provisionId é informado, ele DEVE existir (404 caso contrário),
        // independentemente de virem dados de criação junto.
        if (request.provisionId() != null) {
            if (!seenProvisionIds.add(request.provisionId())) {
                throw new ItemRequestProvisionAlreadyExistsException();
            }
            return provisionPublicApi.findById(request.provisionId())
                    .orElseThrow(ProvisionNotFoundException::new);
        }

        // Serviço novo: exige dados completos e reaproveita por nome ou cria.
        if (!hasText(request.name()) || request.totalValue() == null || !hasText(request.description())) {
            throw new InsufficientProvisionDataException();
        }

        String provisionName = normalizeName(request.name());
        if (!seenNewProvisionNames.add(normalizeNameKey(provisionName))) {
            throw new ItemRequestProvisionAlreadyExistsException();
        }

        return provisionPublicApi.findByNameIgnoreCase(provisionName)
                .orElseGet(() -> provisionPublicApi.createProvision(
                        provisionName,
                        request.totalValue(),
                        request.description().trim()
                ));
    }

    private String normalizeVariation(String variation) {
        return variation == null || variation.isBlank() ? null : variation.trim();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String normalizeName(String name) {
        return name == null ? null : name.trim().replaceAll("\\s+", " ");
    }

    private String normalizeNameKey(String name) {
        return name == null ? "" : name.toLowerCase(Locale.ROOT);
    }
}
