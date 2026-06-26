package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.branchinterface.BranchService;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de
 * {@link net.centroweg.gerenciamentocompras.modules.cr.domain.Branch}.
 *
 * <p>Todas as rotas são prefixadas com {@code /branchs}.</p>
 *
 * @author Leandro
 */
@Tag(name = "ENDPOINTS da entidade BRANCH")
@RequiredArgsConstructor
@RestController
@RequestMapping("/branches")
public class BranchController {

    private final BranchService branchService;

    /**
     * Cria uma nova branch.
     *
     * @param branchRequest corpo da requisição com os dados da branch
     * @return HTTP 201 com a branch criada no corpo da resposta
     */
    @Operation(description = "ENDPOINT responsável pela criação de Branch")
    @PostMapping
    public ResponseEntity<BranchResponse> create(@RequestBody BranchRequest branchRequest){
        return ResponseEntity.status(201)
                .body(branchService.create(branchRequest));
    }

    /**
     * Lista todas as branches cadastradas.
     *
     * @return HTTP 200 com a lista de branches no corpo da resposta
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos Branch")
    @GetMapping
    public ResponseEntity<List<BranchResponse>> listAll(){
        return ResponseEntity.status(200)
                .body(branchService.findAll());
    }

    /**
     * Busca uma branch pelo seu identificador.
     *
     * @param id identificador da branch
     * @return HTTP 200 com a branch encontrada no corpo da resposta
     */
    @Operation(description = "ENDPOINT responsável pela listagem de Branch por id")
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> findById(@PathVariable Long id){
        return ResponseEntity.status(200)
                .body(branchService.findById(id));
    }

    /**
     * Atualiza os dados de uma branch existente.
     *
     * @param id            identificador da branch a ser atualizada
     * @param branchRequest corpo da requisição com os novos dados
     * @return HTTP 200 com a branch atualizada no corpo da resposta
     */
    @Operation(description = "ENDPOINT responsável pela atualização de Branch")
    @PutMapping("/{id}")
    public ResponseEntity<BranchResponse> update(@PathVariable Long id, @RequestBody BranchRequest branchRequest){
        return ResponseEntity.status(200)
                .body(branchService.update(id,branchRequest));
    }

    /**
     * Remove uma branch pelo seu identificador.
     *
     * @param id identificador da branch a ser removida
     * @return HTTP 204 com mensagem de confirmação no corpo da resposta
     */
    @Operation(description = "ENDPOINT responsável pelo delete de Branch")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id){
        return ResponseEntity.status(204)
                .body(branchService.delete(id));
    }

}
