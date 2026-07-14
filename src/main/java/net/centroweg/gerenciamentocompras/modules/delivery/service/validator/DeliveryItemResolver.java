package net.centroweg.gerenciamentocompras.modules.delivery.service.validator;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.domain.exception.InvalidDeliveryItemsException;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;
import net.centroweg.gerenciamentocompras.modules.request.service.api.RequestPublicApi;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeliveryItemResolver {

    private final RequestPublicApi requestPublicApi;

    public List<ItemRequestProduct> resolveProductItems(Request request, List<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return List.copyOf(request.getItemRequestProducts());
        }

        Set<Long> uniqueIds = new LinkedHashSet<>(itemIds);
        List<ItemRequestProduct> items = requestPublicApi.findItemProductsByIds(List.copyOf(uniqueIds));
        if (items.size() != uniqueIds.size()) {
            throw new InvalidDeliveryItemsException("Um ou mais itens de produto da entrega nao foram encontrados.");
        }

        boolean hasItemFromAnotherRequest = items.stream()
                .anyMatch(item -> item.getRequest() == null || !request.getId().equals(item.getRequest().getId()));
        if (hasItemFromAnotherRequest) {
            throw new InvalidDeliveryItemsException("Todos os itens de produto da entrega devem pertencer a solicitacao informada.");
        }

        return items;
    }

    public List<ItemRequestProvision> resolveProvisionItems(Request request, List<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return List.copyOf(request.getItemRequestProvisions());
        }

        Set<Long> uniqueIds = new LinkedHashSet<>(itemIds);
        List<ItemRequestProvision> items = requestPublicApi.findItemProvisionsByIds(List.copyOf(uniqueIds));
        if (items.size() != uniqueIds.size()) {
            throw new InvalidDeliveryItemsException("Um ou mais itens de servico da entrega nao foram encontrados.");
        }

        boolean hasItemFromAnotherRequest = items.stream()
                .anyMatch(item -> item.getRequest() == null || !request.getId().equals(item.getRequest().getId()));
        if (hasItemFromAnotherRequest) {
            throw new InvalidDeliveryItemsException("Todos os itens de servico da entrega devem pertencer a solicitacao informada.");
        }

        return items;
    }
}
