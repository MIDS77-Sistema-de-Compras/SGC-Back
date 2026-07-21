package net.centroweg.gerenciamentocompras.modules.product.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.CreateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.request.UpdateProductRequest;
import net.centroweg.gerenciamentocompras.modules.product.presentation.dto.response.ProductResponse;
import net.centroweg.gerenciamentocompras.modules.product.service.usecases.serviceIntrf.product.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de {@link Product}.
 */
@Tag(name = "ENDPOINTS da entidade produto")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    /**
     * Cria um novo produto.
     * @param request dados do produto.
     * @return produto criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um produto")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    /**
     * Busca um produto pelo seu identificador.
     * @param id identificador do produto.
     * @return produto encontrado, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um produto por id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    /**
     * Lista todos os produtos cadastrados com o nome especificado.
     * @param name nome do produto.
     * @return lista com todos os produtos encontrados, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de produtos por nome")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(productService.findAll(name));
    }

    /**
     * Atualiza um produto existente.
     * @param id identificador do produto.
     * @param request novos dados do produto.
     * @return produto já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um produto")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    /**
     * Remove um produto.
     * @param id identificador do produto.
     * @return resposta sem conteúdo confirmando a remoção.
     */
    @Operation(description = "ENDPOINT responsável por remover um produto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
