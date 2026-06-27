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
 * Controlador REST responsável pelos endpoints de vínculos entre CR e Branch.
 */
@Tag(name = "ENDPOINTS da entidade CR-BRANCH")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cr-branches")
public class CrBranchController {

    private final CrBranchService crBranchService;

    /**
     * Criar um novo vínculo entre CR e Branch.
     * @param request corpo da requisição com os dados do vínculo.
     * @return status HTTP 201(Created) com o corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela criação de CR-Branch")
    @PostMapping
    public ResponseEntity<CrBranchResponse> create(@Valid @RequestBody CrBranchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crBranchService.create(request));
    }

    /**
     * Listar todos os vínculos CR-Branch cadastrados.
     * @return status HTTP 200(OK) com a lista de todas as CR-Branch no corpo da resposta.
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
     * Buscar um vínculo CR-Branch pelo seu identificador.
     * @param id identificador do vínculo CR-Branch.
     * @return status HTTP 200(OK) com a CR-Branch encontrada no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela busca por ID de CR-Branch")
    @GetMapping("/{id}")
    public ResponseEntity<CrBranchResponse> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findById(id));
    }

    /**
     * Atualizar um vínculo CR-Branch existente.
     * @param request corpo da requisição com os novos dados.
     * @param id identificador do vínculo CR-Branch.
     * @return status HTTP 200(OK) com a CR-Branch atualizada no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de CR-Branch")
    @PutMapping("/{id}")
    public ResponseEntity<CrBranchResponse> update(@Valid @RequestBody CrBranchRequest request, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.update(id, request));
    }

    /**
     * Remover um vínculo CR-Branch pelo seu identificador.
     * @param id identificador do vínculo CR-Branch.
     * @return status HTTP 200(OK)
     */
    @Operation(description = "ENDPOINT responsável pelo delete de CR-Branch")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(
                crBranchService.delete(id)
        );
    }

    /**
     * Listar todos os vínculos CR-Branch pertencentes a uma Branch.
     * @param branchId identificador da Branch para pesquisa.
     * @return status HTTP 200 (OK) com as Branches encontradas no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR-Branch por ID de branch")
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<CrBranchResponse>> findCrBranchByBranch(@PathVariable Long branchId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findCrBranchByBranch(branchId));
    }

    /**
     * Atribuir um usuário responsável a um vínculo CR-Branch.
     * @param crBranchId identificador da CR-Branch.
     * @param userId identificador do usuário.
     * @return status HTTP 200 (OK).
     */
    @Operation(description = "ENDPOINT responsável pela atualização de CR-Branch por ID de CR Branch e ID de usuário")
    @PutMapping("/{crBranchId}/responsible/{userId}")
    public ResponseEntity<CrBranchResponse> assignCrBranchResponsible(@PathVariable Long crBranchId, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.assignCrBranchResponsible(crBranchId, userId));
    }

    /**
     * Remover o usuário responsável de um vínculo CR-Branch.
     * @param crBranchId identificador da CR-Branch.
     * @return status HTTP 200 (OK).
     */
    @Operation(description = "ENDPOINT responsável pelo delete de CR-Branch por ID de CR Branch")
    @DeleteMapping("/{crBranchId}/responsible")
    public ResponseEntity<CrBranchResponse> removeCrBranchResponsible(@PathVariable Long crBranchId) {
        return ResponseEntity.ok(
                crBranchService.removeCrBranchResponsible(crBranchId)
        );
    }

}
