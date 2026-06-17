package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.ItemRequestProvisionService;

@Tag(name = "ENDPOINTS da entidade ITEM-REQUEST-PROVISION")
@RestController
@RequiredArgsConstructor
@RequestMapping("/item-provision-requests")
public class ItemRequestProvisionController {

    private final ItemRequestProvisionService itemRequestProvisionService;

    @Operation(description = "ENDPOINT responsável pela criação de Item Request Provision")
    @PostMapping
    public ResponseEntity<ItemRequestProvisionResponse> addItem(@RequestBody ItemRequestProvisionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(itemRequestProvisionService.addItemToProvisionRequest(request));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de todos Item Request Provision por Request")
    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<ItemRequestProvisionResponse>> findAllItems(@PathVariable("requestId") Long requestId){
        return ResponseEntity.status(HttpStatus.OK)
            .body(itemRequestProvisionService.findAllProvisionRequestItems(requestId));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de Item Request Provision por id e Request")
    @GetMapping("/request/{requestId}/{itemId}")
    public ResponseEntity<ItemRequestProvisionResponse> findItemByIdAndRequestId(@PathVariable("requestId") Long requestId, @PathVariable("itemId") Long itemId){
        return ResponseEntity.status(HttpStatus.OK)
            .body(itemRequestProvisionService.findProvisionRequestItemById(requestId, itemId));
    }

    @Operation(description = "ENDPOINT responsável pela atualização de Item Request Provision")
    @PutMapping("/request/{itemId}")
    public ResponseEntity<ItemRequestProvisionResponse> updateItem(@PathVariable("itemId") Long itemId, @RequestBody ItemRequestProvisionRequest request){
        return ResponseEntity.status(HttpStatus.OK)
            .body(itemRequestProvisionService.updateItemFromProvisionRequest(itemId, request));
    }

    @Operation(description = "ENDPOINT responsável pelo delete de Item Request Provision")
    @DeleteMapping("/request/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") Long itemId){
        itemRequestProvisionService.deleteItemFromProvisionRequest(itemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
