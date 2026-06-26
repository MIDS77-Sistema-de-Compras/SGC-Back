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
 * Controller responsável pelo gerenciamento dos endpoints
 * relacionados à entidade Status.
 *
 * <p>Disponibiliza operações de cadastro, listagem,
 * busca, atualização e remoção de status.</p>
 *
 * @author André
 * @since 1.0
 */
@Tag(name = "ENDPOINTS da entidade STATUS")
@RequestMapping("/status")
@RestController
@RequiredArgsConstructor
public class StatusController {
    /**
     * Serviço responsável pelas regras de negócio da entidade Status.
     */
    private final StatusService statusService;
    /**
     * Realiza o cadastro de um novo status.
     *
     * @param statusRequest dados do status a ser cadastrado
     * @return status criado
     */
    @Operation(description = "ENDPOINT responsável pela criação de Status")
    @PostMapping
    public ResponseEntity<StatusResponse> addStatus(@Valid @RequestBody StatusRequest statusRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(statusService.createStatus(statusRequest));
    }
    /**
     * Lista todos os status cadastrados.
     *
     * @return lista de status
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos Status")
    @GetMapping
    public ResponseEntity<List<StatusResponse>> listStatus () {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.findAllStatus());
    }
    /**
     * Busca um status pelo identificador.
     *
     * @param id identificador do status
     * @return status encontrado
     */
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

    /**
     * Atualiza os dados de um status existente.
     *
     * @param id identificador do status
     * @param statusRequest novos dados do status
     * @return status atualizado
     */
    @Operation(description = "ENDPOINT responsável pela atualização de Status")
    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse> editStatus (@PathVariable Long id, @Valid @RequestBody StatusRequest statusRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.editStatus(id, statusRequest));
    }
    /**
     * Remove um status pelo identificador.
     *
     * @param id identificador do status
     * @return resposta sem conteúdo
     */
    @Operation(description = "ENDPOINT responsável pelo delete de Status")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus (@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}