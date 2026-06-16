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

@Tag(name = "ENDPOINTS da entidade BRANCH")
@RequiredArgsConstructor
@RestController
@RequestMapping("/branchs")
public class BranchController {

    private final BranchService branchService;


    @Operation(description = "ENDPOINT responsável pela criação de Branch")
    @PostMapping
    public ResponseEntity<BranchResponse> create(@RequestBody BranchRequest branchRequest){
        return ResponseEntity.status(201)
                .body(branchService.create(branchRequest));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de todos Branch")
    @GetMapping
    public ResponseEntity<List<BranchResponse>> listAll(){
        return ResponseEntity.status(200)
                .body(branchService.findAll());
    }


    @Operation(description = "ENDPOINT responsável pela listagem de Branch por id")
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> findById(@PathVariable Long id){
        return ResponseEntity.status(200)
                .body(branchService.findById(id));
    }


    @Operation(description = "ENDPOINT responsável pela atualização de Branch")
    @PutMapping("/{id}")
    public ResponseEntity<BranchResponse> update(@PathVariable Long id, @RequestBody BranchRequest branchRequest){
        return ResponseEntity.status(200)
                .body(branchService.update(id,branchRequest));
    }


    @Operation(description = "ENDPOINT responsável pelo delete de Branch")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id){
        return ResponseEntity.status(204)
                .body(branchService.delete(id));
    }

}
