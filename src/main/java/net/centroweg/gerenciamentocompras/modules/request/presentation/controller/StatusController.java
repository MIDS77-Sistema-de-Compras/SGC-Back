package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de {@link Status}.
 */
@Tag(name = "ENDPOINTS da entidade status")
@RequestMapping("/status")
@RestController
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    /**
     * Cria um novo status.
     * @param statusRequest dados do status.
     * @return status criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um status")
    @PostMapping
    public ResponseEntity<StatusResponse> addStatus(@Valid @RequestBody StatusRequest statusRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(statusService.createStatus(statusRequest));
    }

    /**
     * Lista todos os status cadastrados.
     * @return lista com todos os status encontrados, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos os status")
    @GetMapping
    public ResponseEntity<List<StatusResponse>> listStatus () {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.findAllStatus());
    }

    /**
     * Busca um status pelo seu identificador.
     * @param id identificador do status.
     * @return status encontrado, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um status por id")
    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse> findStatusById (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.findStatusById(id));
    }

    /**
     * Busca um status pelo nome.
     * @param statusName nome do status.
     * @return status encontrado, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um status por nome")
    @GetMapping("/statusName/{statusName}")
    public ResponseEntity<StatusResponse> findStatusByName(@PathVariable String statusName){
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.findStatusByName(statusName));
    }

    /**
     * Atualiza um status existente.
     * @param id identificador do status.
     * @param statusRequest novos dados do status.
     * @return status já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um status")
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse> editStatus (@PathVariable Long id, @Valid @RequestBody StatusRequest statusRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.editStatus(id, statusRequest));
    }

    /**
     * Remove um status.
     * @param id identificador do status.
     * @return resposta sem conteúdo confirmando a remoção.
     */
    @Operation(description = "ENDPOINT responsável por deletar um status")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus (@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
