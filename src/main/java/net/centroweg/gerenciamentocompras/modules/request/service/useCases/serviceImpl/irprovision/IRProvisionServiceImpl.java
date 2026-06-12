package net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceImpl.irprovision;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.ItemRequestProvisionService;

@Service
@RequiredArgsConstructor
public class IRProvisionServiceImpl implements ItemRequestProvisionService {

    private final AddItemToRequestProvisionServiceImpl addItemToRequestProvision;
    private final FindAllItemsRequestProvisionServiceImpl findAllItemsRequestProvision;
    private final FindItemRequestProvisionByIdServiceImpl findItemRequestProvisionById;
    private final UpdateItemRequestProvisionServiceImpl updateItemRequestProvision;
    private final DeleteRequestProvisionItemServiceImpl deleteRequestProvisionItem;

    @Override
    public ItemRequestProvisionResponse addItemToProvisionRequest(ItemRequestProvisionRequest request) {
        return addItemToRequestProvision.addItem(request);
    }

    @Override
    public List<ItemRequestProvisionResponse> findAllProvisionRequestItems(Long requestId) {
        return findAllItemsRequestProvision.findAll(requestId);
    }

    @Override
    public ItemRequestProvisionResponse findProvisionRequestItemById(Long requestId, Long itemId) {
        return findItemRequestProvisionById.findById(requestId, itemId);
    }

    @Override
    public ItemRequestProvisionResponse updateItemFromProvisionRequest(Long itemId, ItemRequestProvisionRequest request) {
        return updateItemRequestProvision.updateItem(itemId, request);
    }

    @Override
    public void deleteItemFromProvisionRequest(Long itemId) {
        deleteRequestProvisionItem.deleteItem(itemId);
    }

}
