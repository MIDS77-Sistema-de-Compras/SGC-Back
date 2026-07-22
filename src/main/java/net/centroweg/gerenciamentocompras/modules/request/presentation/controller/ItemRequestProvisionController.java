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
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de {@link ItemRequestProvision}.
 */
@Tag(name = "ENDPOINTS da entidade item de serviço")
@RestController
@RequiredArgsConstructor
@RequestMapping("/item-provision-requests")
public class ItemRequestProvisionController {

    private final ItemRequestProvisionService itemRequestProvisionService;

    /**
     * Cria um novo item de serviço.
     * @param request dados do item de serviço.
     * @return item de serviço criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um item de serviço")
    @PostMapping
    public ResponseEntity<ItemRequestProvisionResponse> addItem(@RequestBody ItemRequestProvisionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(itemRequestProvisionService.addItemToProvisionRequest(request));
    }

    /**
     * Lista todos os itens de serviço associados a uma requisição.
     * @param requestId identificador da requisição.
     * @return lista com todos os itens de serviço encontrados, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela listagem dos itens de serviço por requisição")
    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<ItemRequestProvisionResponse>> findAllItems(@PathVariable("requestId") Long requestId){
        return ResponseEntity.status(HttpStatus.OK)
            .body(itemRequestProvisionService.findAllProvisionRequestItems(requestId));
    }

    /**
     * Busca um item de serviço pelo seu identificador, garantindo que pertença à requisição informada.
     * @param requestId identificador da requisição.
     * @param itemId identificador do item.
     * @return item de serviço encontrado, caso exista e pertença à requisição informada.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um item de serviço por id")
    @GetMapping("/request/{requestId}/{itemId}")
    public ResponseEntity<ItemRequestProvisionResponse> findItemByIdAndRequestId(@PathVariable("requestId") Long requestId, @PathVariable("itemId") Long itemId){
        return ResponseEntity.status(HttpStatus.OK)
            .body(itemRequestProvisionService.findProvisionRequestItemById(requestId, itemId));
    }

    /**
     * Atualiza um item de serviço existente.
     * @param itemId identificador do item.
     * @param request novos dados do item de serviço.
     * @return item de serviço já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um item de serviço")
    @PutMapping("/request/{itemId}")
    public ResponseEntity<ItemRequestProvisionResponse> updateItem(@PathVariable("itemId") Long itemId, @RequestBody ItemRequestProvisionRequest request){
        return ResponseEntity.status(HttpStatus.OK)
            .body(itemRequestProvisionService.updateItemFromProvisionRequest(itemId, request));
    }

    /**
     * Remove um item de serviço.
     * @param itemId identificador do item.
     * @return resposta sem conteúdo confirmando a remoção.
     */
    @Operation(description = "ENDPOINT responsável por deletar um item de serviço")
    @DeleteMapping("/request/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") Long itemId){
        itemRequestProvisionService.deleteItemFromProvisionRequest(itemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
