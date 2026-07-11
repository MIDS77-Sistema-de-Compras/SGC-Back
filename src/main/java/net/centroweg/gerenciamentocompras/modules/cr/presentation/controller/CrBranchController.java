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
 * Controlador REST responsável pelos endpoints de vínculos entre CR e filial.
 */
@Tag(name = "ENDPOINTS da entidade CR-filial")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cr-branches")
public class CrBranchController {

    private final CrBranchService crBranchService;

    /**
     * Criar um novo vínculo entre CR e filial.
     * @param request corpo da requisição com os dados do vínculo.
     * @return vínculo criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de CR-filial")
    @PostMapping
    public ResponseEntity<CrBranchResponse> create(@Valid @RequestBody CrBranchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crBranchService.create(request));
    }

    /**
     * Listar todos os vínculos CR-filial cadastrados.
     * @return lista de todas as CR-filiais encontradas.
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
     * Buscar um vínculo CR-filial pelo seu identificador.
     * @param id identificador do vínculo CR-filial.
     * @return CR-filial encontrada.
     */
    @Operation(description = "ENDPOINT responsável pela busca por ID de CR-filial")
    @GetMapping("/{id}")
    public ResponseEntity<CrBranchResponse> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findById(id));
    }

    /**
     * Atualizar um vínculo CR-filial existente.
     * @param request corpo da requisição com os novos dados.
     * @param id identificador do vínculo CR-filial.
     * @return CR-filial atualizada no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela atualização das CR-filiais")
    @PutMapping("/{id}")
    public ResponseEntity<CrBranchResponse> update(@Valid @RequestBody CrBranchRequest request, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.update(id, request));
    }

    /**
     * Remover um vínculo CR-filial pelo seu identificador.
     * @param id identificador do vínculo CR-filial.
     * @return mensagem de confirmação da remoção.
     */
    @Operation(description = "ENDPOINT responsável por deletar as CR-filiais")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(
                crBranchService.delete(id)
        );
    }

    /**
     * Listar todos os vínculos CR-filial pertencentes a uma filial.
     * @param branchId identificador da filial para pesquisa.
     * @return lista com as filiais encontradas.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR-filial por ID da filial")
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<CrBranchResponse>> findCrBranchByBranch(@PathVariable Long branchId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findCrBranchByBranch(branchId));
    }

    /**
     * Atribuir um usuário responsável a um vínculo CR-filial.
     * @param crBranchId identificador da CR-filial.
     * @param userId identificador do usuário.
     * @return CR-filial com o usuário atribuido.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de CR-filial por ID de CR-filial e ID de usuário")
    @PutMapping("/{crBranchId}/responsible/{userId}")
    public ResponseEntity<CrBranchResponse> assignCrBranchResponsible(@PathVariable Long crBranchId, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.assignCrBranchResponsible(crBranchId, userId));
    }

    /**
     * Remover o usuário responsável de um vínculo CR-filial.
     * @param crBranchId identificador da CR-filial.
     * @return CR-filial com o usuário removido.
     */
    @Operation(description = "ENDPOINT responsável por deletar das CR-filiais por ID de CR-filial")
    @DeleteMapping("/{crBranchId}/responsible")
    public ResponseEntity<CrBranchResponse> removeCrBranchResponsible(@PathVariable Long crBranchId) {
        return ResponseEntity.ok(
                crBranchService.removeCrBranchResponsible(crBranchId)
        );
    }

}
