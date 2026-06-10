package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.ItemRequestProvisionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item-provision-requests")
public class ItemRequestProvisionController {
    
    private final ItemRequestProvisionService itemRequestProvisionService;

    @PostMapping
    public ResponseEntity<ItemRequestProvisionResponse> addItem(@RequestBody ItemRequestProvisionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(itemRequestProvisionService.addItemToProvisionRequest(request));
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<ItemRequestProvisionResponse>> findAllItems(@PathVariable("requestId") Long requestId){
        return ResponseEntity.status(HttpStatus.OK)
            .body(itemRequestProvisionService.findAllProvisionRequestItems(requestId));
    }

    @GetMapping("/request/{requestId}/{itemId}")
    public ResponseEntity<ItemRequestProvisionResponse> findItemByIdAndRequestId(@PathVariable("requestId") Long requestId, @PathVariable("itemID") Long itemId){
        return ResponseEntity.status(HttpStatus.OK)
            .body(itemRequestProvisionService.findProvisionRequestItemById(requestId, itemId));
    }

    @PutMapping("/request/{itemId}")
    public ResponseEntity<ItemRequestProvisionResponse> updateItem(@PathVariable("itemId") Long itemId, @RequestBody ItemRequestProvisionRequest request){
        return ResponseEntity.status(HttpStatus.OK)
            .body(itemRequestProvisionService.updateItemFromProvisionRequest(itemId, request));
    }

    @DeleteMapping("/request/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") Long itemId){
        itemRequestProvisionService.deleteItemFromProvisionRequest(itemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
