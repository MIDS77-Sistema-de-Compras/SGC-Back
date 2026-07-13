package net.centroweg.gerenciamentocompras.modules.delivery.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.ConfirmDeliveryReceiverRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.CreateDeliveryRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.request.UpdateDeliveryRequest;
import net.centroweg.gerenciamentocompras.modules.delivery.presentation.dto.response.DeliveryResponse;
import net.centroweg.gerenciamentocompras.modules.delivery.service.usecases.serviceIntrf.DeliveryService;
import net.centroweg.gerenciamentocompras.shared.security.annotation.CanManagePurchaseItems;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ENDPOINTS da entidade DELIVERY")
@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(description = "Cria uma entrega com exatamente dois recebedores.")
    @PostMapping
    @CanManagePurchaseItems
    public ResponseEntity<DeliveryResponse> create(@Valid @RequestBody CreateDeliveryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(request));
    }

    @Operation(description = "Lista entregas com filtros opcionais.")
    @GetMapping
    @CanManagePurchaseItems
    public ResponseEntity<List<DeliveryResponse>> findAll(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Long requestId,
            @RequestParam(required = false) Long receiverId
    ) {
        return ResponseEntity.ok(deliveryService.findAll(active, requestId, receiverId));
    }

    @Operation(description = "Busca uma entrega por id.")
    @GetMapping("/{id}")
    @CanManagePurchaseItems
    public ResponseEntity<DeliveryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    @Operation(description = "Atualiza uma entrega e seus recebedores.")
    @PutMapping("/{id}")
    @CanManagePurchaseItems
    public ResponseEntity<DeliveryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDeliveryRequest request
    ) {
        return ResponseEntity.ok(deliveryService.update(id, request));
    }

    @Operation(description = "Inativa uma entrega sem apagar o historico de recebedores.")
    @DeleteMapping("/{id}")
    @CanManagePurchaseItems
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Confirma o recebimento por um recebedor associado a entrega.")
    @PatchMapping("/{deliveryId}/receivers/{userId}/confirm")
    public ResponseEntity<DeliveryResponse> confirmReceiver(
            @PathVariable Long deliveryId,
            @PathVariable Long userId,
            @Valid @RequestBody ConfirmDeliveryReceiverRequest request
    ) {
        return ResponseEntity.ok(deliveryService.confirmReceiver(deliveryId, userId, request));
    }
}
