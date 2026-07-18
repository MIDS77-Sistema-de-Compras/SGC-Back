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
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento do {@link CrBranch}.
 */
@Tag(name = "ENDPOINTS da entidade CR-filial")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cr-branches")
public class CrBranchController {

    private final CrBranchService crBranchService;

    /**
     * Cria um novo vínculo CR-filial.
     * @param request dados do vínculo.
     * @return vínculo criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um CR-filial")
    @PostMapping
    public ResponseEntity<CrBranchResponse> create(@Valid @RequestBody CrBranchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crBranchService.create(request));
    }

    /**
     * Lista todos os vínculos CR-filial cadastrados.
     * @return lista com todos os vínculos encontrados.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR-filiais")
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
     * Busca um vínculo CR-filial pelo seu identificador.
     * @param id identificador do vínculo.
     * @return vínculo encontrado.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um CR-filial pelo identificador")
    @GetMapping("/{id}")
    public ResponseEntity<CrBranchResponse> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findById(id));
    }

    /**
     * Atualiza um vínculo CR-filial existente.
     * @param request novos dados do vínculo.
     * @param id identificador do vínculo.
     * @return vínculo já atualizada.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um CR-filial")
    @PutMapping("/{id}")
    public ResponseEntity<CrBranchResponse> update(@Valid @RequestBody CrBranchRequest request, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.update(id, request));
    }

    /**
     * Remove um vínculo CR-filial.
     * @param id identificador do vínculo.
     * @return mensagem de confirmação da remoção.
     */
    @Operation(description = "ENDPOINT responsável por remover um CR-filial")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(
                crBranchService.delete(id)
        );
    }

    /**
     * Listar todos os vínculos CR-filial pertencentes a uma filial.
     * @param branchId identificador da filial.
     * @return lista com os vínculos encontrados.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR-filial por ID da filial")
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<CrBranchResponse>> findCrBranchByBranch(@PathVariable Long branchId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findCrBranchByBranch(branchId));
    }

    /**
     * Atribui um usuário a um vínculo CR-filial.
     * @param crBranchId identificador do CR-filial.
     * @param userId identificador do usuário.
     * @return vínculo já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de CR-filial por ID de CR-filial e ID de usuário")
    @PutMapping("/{crBranchId}/responsible/{userId}")
    public ResponseEntity<CrBranchResponse> assignCrBranchResponsible(@PathVariable Long crBranchId, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.assignCrBranchResponsible(crBranchId, userId));
    }

    /**
     * Remover um usuário responsável de um vínculo CR-filial.
     * @param crBranchId identificador do CR-filial.
     * @return vínculo já atualizado.
     */
    @Operation(description = "ENDPOINT responsável por deletar um ou mais responsáveis dos CR-filiais")
    @DeleteMapping("/{crBranchId}/responsible")
    public ResponseEntity<CrBranchResponse> removeCrBranchResponsible(@PathVariable Long crBranchId) {
        return ResponseEntity.ok(
                crBranchService.removeCrBranchResponsible(crBranchId)
        );
    }
}