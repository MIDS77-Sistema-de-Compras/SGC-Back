package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/cr-branches")
public class CrBranchController {

    private final CrBranchService crBranchService;

    @PostMapping
    public ResponseEntity<CrBranchResponse> create(@Valid @RequestBody CrBranchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crBranchService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CrBranchResponse>> findAll(
            @RequestParam(required = false) String crCode,
            @RequestParam(required = false) String crName,
            @RequestParam(required = false) String responsibleName
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

    @GetMapping("/{id}")
    public ResponseEntity<CrBranchResponse> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CrBranchResponse> update(@Valid @RequestBody CrBranchRequest request, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(
                crBranchService.delete(id)
        );
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<CrBranchResponse>> findCrBranchByBranch(@PathVariable Long branchId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.findCrBranchByBranch(branchId));
    }

    @PutMapping("/{crBranchId}/responsible/{userId}")
    public ResponseEntity<CrBranchResponse> assignCrBranchResponsible(@PathVariable Long crBranchId, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(crBranchService.assignCrBranchResponsible(crBranchId, userId));
    }

    @DeleteMapping("/{crBranchId}/responsible")
    public ResponseEntity<CrBranchResponse> removeCrBranchResponsible(@PathVariable Long crBranchId) {
        return ResponseEntity.ok(
                crBranchService.removeCrBranchResponsible(crBranchId)
        );
    }

}
