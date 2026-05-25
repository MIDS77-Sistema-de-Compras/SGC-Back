package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.BranchRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.BranchResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.branchservice.branchinterface.BranchService;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/branchs")
public class BranchController {

    private final BranchService branchService;


    @PostMapping
    public ResponseEntity<BranchResponse> create(@RequestBody BranchRequest branchRequest){
        return ResponseEntity.status(201)
                .body(branchService.create(branchRequest));
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> listAll(){
        return ResponseEntity.status(200)
                .body(branchService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponse> findById(@PathVariable long id){
        return ResponseEntity.status(200)
                .body(branchService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchResponse> update(@PathVariable long id, @RequestBody BranchRequest branchRequest){
        return ResponseEntity.status(200)
                .body(branchService.update(id,branchRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable long id){
        return ResponseEntity.status(204)
                .body(branchService.delete(id));
    }

}
