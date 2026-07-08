package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Branch;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.branchinterface.BranchService;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de {@link Branch}.
 */
@Tag(name = "ENDPOINTS da entidade filial")
@RequiredArgsConstructor
@RestController
@RequestMapping("/branches")
public class BranchController {

    private final BranchService branchService;

    /**
     * Criar uma nova filial.
     * @param branchRequest corpo da requisição com os dados da filial.
     * @return HTTP 201 com a filial criada no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela criação de filial")
    @PostMapping
    public ResponseEntity<BranchResponse> create(@RequestBody BranchRequest branchRequest){
        return ResponseEntity.status(201)
                .body(branchService.create(branchRequest));
    }

    /**
     * Listar todas as filiais cadastradas.
     * @return HTTP 200 com a lista de filiais no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todas as filiais")
    @GetMapping
    public ResponseEntity<List<BranchResponse>> listAll(){
        return ResponseEntity.status(200)
                .body(branchService.findAll());
    }

    /**
     * Busca uma filial pelo seu identificador.
     * @param id identificador da filial.
     * @return HTTP 200 com a filial encontrada no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de filial por id")
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> findById(@PathVariable Long id){
        return ResponseEntity.status(200)
                .body(branchService.findById(id));
    }

    /**
     * Atualizar os dados de uma filial existente.
     * @param id identificador da filial a ser atualizada.
     * @param branchRequest corpo da requisição com os novos dados da filial.
     * @return HTTP 200 com a filial atualizada no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de filiais")
    @PutMapping("/{id}")
    public ResponseEntity<BranchResponse> update(@PathVariable Long id, @RequestBody BranchRequest branchRequest){
        return ResponseEntity.status(200)
                .body(branchService.update(id,branchRequest));
    }

    /**
     * Remover uma filial pelo seu identificador.
     * @param id identificador da filial a ser removida.
     * @return HTTP 204 com mensagem de confirmação no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável por deletar as filiais")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id){
        return ResponseEntity.status(204)
                .body(branchService.delete(id));
    }

}
