package net.centroweg.gerenciamentocompras.modules.product.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ENDPOINTS da entidade PRODUCT")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @Operation(description = "ENDPOINT responsável pela criação de Product")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de Product por id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de todos Product")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(productService.findAll(name));
    }

    @Operation(description = "ENDPOINT responsável pela atualização de Product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @Operation(description = "ENDPOINT responsável pelo delete de Product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}