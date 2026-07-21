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
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProduct;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de {@link ItemRequestProduct}.
 */
@Tag(name = "ENDPOINTS da entidade item de produto")
@RestController
@RequestMapping("/item-request-products")
@RequiredArgsConstructor
public class ItemRequestProductController {

    private final ItemRequestProductService itemRequestProductService;

    /**
     * Cria um novo item de produto.
     * @param itemRequestProductRequest dados do item de produto.
     * @return item de produto criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um item de produto")
    @PostMapping
    public ResponseEntity<ItemRequestProductResponse> createItemRequestProduct(@Valid @RequestBody ItemRequestProductRequest itemRequestProductRequest){
        return ResponseEntity.status(201).body(itemRequestProductService.createRequestProduct(itemRequestProductRequest));
    }

    /**
     * Busca um item de produto pelo seu identificador.
     * @param id identificador do item de produto.
     * @return item de produto encontrado, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um item de produto por id")
    @GetMapping("/{id}")
    public ResponseEntity<ItemRequestProductResponse> findByIdItemRequestProduct(@PathVariable Long id){
        return ResponseEntity.status(200).body(itemRequestProductService.findRequestProductById(id));
    }

    /**
     * Lista todos os itens de produto cadastrados.
     * @return lista com todos os itens de produto encontrados, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos os itens de produto")
    @GetMapping
    public ResponseEntity<List<ItemRequestProductResponse>> listItemRequestProduct(){
        return ResponseEntity.status(200).body(itemRequestProductService.findAllRequestProduct());
    }

    /**
     * Atualiza um item de produto existente.
     * @param id identificador do item de produto.
     * @param itemRequestProductRequest novos dados do item de produto.
     * @return item de produto já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um item de produto")
    @PutMapping("/{id}")
    public ResponseEntity<ItemRequestProductResponse> updateItemRequestProduct(@PathVariable Long id, @Valid @RequestBody ItemRequestProductRequest itemRequestProductRequest){
        return ResponseEntity.status(200).body(itemRequestProductService.updateRequestProduct(itemRequestProductRequest, id));
    }

    /**
     * Remove um item de produto.
     * @param id identificador do item de produto.
     * @return resposta sem conteúdo confirmando a remoção.
     */
    @Operation(description = "ENDPOINT responsável por deletar um item de produto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemRequestProduct(@PathVariable Long id){
        itemRequestProductService.deleteRequestProduct(id);
        return ResponseEntity.status(204).build();
    }
}
