package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.ItemRequestProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ENDPOINTS da entidade ITEM-REQUEST-PRODUCT")
@RestController
@RequestMapping("/item-request-products")
@RequiredArgsConstructor
public class ItemRequestProductController {

    private final ItemRequestProductService itemRequestProductService;

    @Operation(description = "ENDPOINT responsável pela criação de Item Request Product")
    @PostMapping
    public ResponseEntity<ItemRequestProductResponse> createItemRequestProduct(@Valid @RequestBody ItemRequestProductRequest itemRequestProductRequest){
        return ResponseEntity.status(201).body(itemRequestProductService.createRequestProduct(itemRequestProductRequest));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de Item Request Product por id")
    @GetMapping("/{id}")
    public ResponseEntity<ItemRequestProductResponse> findByIdItemRequestProduct(@PathVariable Long id){
        return ResponseEntity.status(200).body(itemRequestProductService.findRequestProductById(id));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de todos Item Request Product")
    @GetMapping
    public ResponseEntity<List<ItemRequestProductResponse>> listItemRequestProduct(){
        return ResponseEntity.status(200).body(itemRequestProductService.findAllRequestProduct());
    }

    @Operation(description = "ENDPOINT responsável pela atualização de Item Request Product")
    @PutMapping("/{id}")
    public ResponseEntity<ItemRequestProductResponse> updateItemRequestProduct(@PathVariable Long id, @Valid @RequestBody ItemRequestProductRequest itemRequestProductRequest){
        return ResponseEntity.status(200).body(itemRequestProductService.updateRequestProduct(itemRequestProductRequest, id));
    }

    @Operation(description = "ENDPOINT responsável pelo delete de Item Request Product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemRequestProduct(@PathVariable Long id){
        itemRequestProductService.deleteRequestProduct(id);
        return ResponseEntity.status(204).build();
    }
}
