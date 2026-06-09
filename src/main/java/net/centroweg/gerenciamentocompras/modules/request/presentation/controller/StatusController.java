package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ENDPOINTS da entidade STATUS")
@RequestMapping("/status")
@RestController
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @Operation(description = "ENDPOINT responsável pela criação de Status")
    @PostMapping
    public ResponseEntity<StatusResponse> addStatus(@Valid @RequestBody StatusRequest statusRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(statusService.createStatus(statusRequest));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de todos Status")
    @GetMapping
    public ResponseEntity<List<StatusResponse>> listStatus () {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.findAllStatus());
    }

    @Operation(description = "ENDPOINT responsável pela listagem de Status por id")
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse> findStatusById (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.findStatusById(id));
    }

    @Operation(description = "ENDPOINT responsável por encontrar Status por id")
    @GetMapping("/statusName/{statusName}")
    public ResponseEntity<StatusResponse> findStatusByName(@PathVariable String statusName){
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.findStatusByName(statusName));
    }

    @Operation(description = "ENDPOINT responsável pela atualização de Status")
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse> editStatus (@PathVariable Long id, @Valid @RequestBody StatusRequest statusRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.editStatus(id, statusRequest));
    }

    @Operation(description = "ENDPOINT responsável pelo delete de Status")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus (@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}