package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchFilterRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrBranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrBranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crbranchservice.crbranchinterface.CrBranchService;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelos endpoints de vínculos entre CR e filial
 */
@Tag(name = "ENDPOINTS da entidade CR-BRANCH")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cr-branches")
public class CrBranchController {

    private final CrBranchService crBranchService;

    /**
     * Cria um novo vínculo entre CR e filial.
     *
     * @param request
     * @return status HTTP 201 (Created)
     */
    @Operation(description = "ENDPOINT responsável pela criação de CR-Branch")
    @PostMapping
    public ResponseEntity<CrBranchResponse> create(@Valid @RequestBody CrBranchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crBranchService.create(request));
    }

    /**
     * Lista todos os vínculos CR-filial cadastrados.
     *
     * @return status HTTP 200 (OK)
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR-Branch")
    @GetMapping
    public ResponseEntity<List<CrBranchResponse>> findAll(
            @RequestParam(required = false) String crCode,
            @RequestParam(required = false) String crName,
            @RequestParam(required = false) List<String> responsibleName
    ) {
        CrBranchFilterRequest filter =
                new CrBranchFilterRequest(
                        crCode,
                        crName,
                        responsibleName
                );

        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findAll(filter));
    }

    /**
     * Busca um vínculo CR-filial pelo seu identificador
     *
     * @param id
     * @return status HTTP 200 (OK)
     */
    @Operation(description = "ENDPOINT responsável pela busca por ID de CR-Branch")
    @GetMapping("/{id}")
    public ResponseEntity<CrBranchResponse> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findById(id));
    }

    /**
     * Atualiza um vínculo CR-filial existente.
     *
     * @param request
     * @param id
     * @return status HTTP 200 (OK)
     */
    @Operation(description = "ENDPOINT responsável pela atualização de CR-Branch")
    @PutMapping("/{id}")
    public ResponseEntity<CrBranchResponse> update(@Valid @RequestBody CrBranchRequest request, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.update(id, request));
    }

    /**
     * Remove um vínculo CR-filial pelo seu identificador.
     *
     * @param id
     * @return status HTTP 200 (OK)
     */

    @Operation(description = "ENDPOINT responsável pelo delete de CR-Branch")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(
                crBranchService.delete(id)
        );
    }

    /**
     * Lista todos os vínculos CR-filial pertencentes a uma filial
     *
     * @param branchId
     * @return status HTTP 200 (OK)
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR-Branch por ID de branch")
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<CrBranchResponse>> findCrBranchByBranch(@PathVariable Long branchId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findCrBranchByBranch(branchId));
    }

    /**
     * Atribui um usuário responsável a um vínculo CR-filial.
     *
     * @param crBranchId
     * @param userId
     * @return status HTTP 200 (OK)
     */
    @Operation(description = "ENDPOINT responsável pela atualização de CR-Branch por ID de CR Branch e ID de usuário")
    @PutMapping("/{crBranchId}/responsible/{userId}")
    public ResponseEntity<CrBranchResponse> assignCrBranchResponsible(@PathVariable Long crBranchId, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.assignCrBranchResponsible(crBranchId, userId));
    }

    /**
     * Remove o usuário responsável de um vínculo CR-filial
     *
     * @param crBranchId
     * @return status HTTP 200 (OK)
     */
    @Operation(description = "ENDPOINT responsável pelo delete de CR-Branch por ID de CR Branch")
    @DeleteMapping("/{crBranchId}/responsible")
    public ResponseEntity<CrBranchResponse> removeCrBranchResponsible(@PathVariable Long crBranchId) {
        return ResponseEntity.ok(
                crBranchService.removeCrBranchResponsible(crBranchId)
        );
    }

}
