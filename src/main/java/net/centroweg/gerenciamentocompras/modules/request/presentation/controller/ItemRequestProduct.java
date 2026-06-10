package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.ItemRequestProductRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.ItemRequestProductResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.ItemRequestProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item-request-products")
@RequiredArgsConstructor
public class ItemRequestProduct {

    private final ItemRequestProductService itemRequestProductService;

    @PostMapping
    public ResponseEntity<ItemRequestProductResponse> createItemRequestProduct(@Valid @RequestBody ItemRequestProductRequest itemRequestProductRequest){
        return ResponseEntity.status(201).body(itemRequestProductService.createRequestProduct(itemRequestProductRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemRequestProductResponse> findByIdItemRequestProduct(@PathVariable Long id){
        return ResponseEntity.status(200).body(itemRequestProductService.findRequestProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestProductResponse>> listItemRequestProduct(){
        return ResponseEntity.status(200).body(itemRequestProductService.findAllRequestProduct());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemRequestProductResponse> updateItemRequestProduct(@PathVariable Long id, @Valid @RequestBody ItemRequestProductRequest itemRequestProductRequest){
        return ResponseEntity.status(200).body(itemRequestProductService.updateRequestProduct(itemRequestProductRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemRequestProduct(@PathVariable Long id){
        itemRequestProductService.deleteRequestProduct(id);
        return ResponseEntity.status(204).build();
    }
}
