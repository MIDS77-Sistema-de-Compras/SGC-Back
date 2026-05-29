package net.centroweg.gerenciamentocompras.modules.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.RoleIntrf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ENDPOINTS da entidade ROLE")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleIntrf role;

    @Operation(description = "ENDPOINT responsável pela criação de Role")
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRole roleRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(role.createRole(roleRequest));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de todos Role")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> listRole(){
        return ResponseEntity.ok(role.listRole());
    }

    @Operation(description = "ENDPOINT responsável pela listagem de Role por id")
    @GetMapping("/RoleId/{RoleId}")
    public ResponseEntity<RoleResponse> findRoleById(@PathVariable Long RoleId){
        return ResponseEntity.ok(role.findRoleById(RoleId));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de Role por nome")
    @GetMapping("/RoleName/{RoleName}")
    public ResponseEntity<List<RoleResponse>> findRoleByName(@PathVariable String RoleName){
        return ResponseEntity.ok(role.findRoleByName(RoleName));
    }

    @Operation(description = "ENDPOINT responsável pela atualização de Role")
    @PutMapping("/RoleId/{RoleId}")
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody CreateRole roleRequest, @PathVariable Long RoleId){
        return ResponseEntity.ok(role.updateRole(RoleId, roleRequest));
    }

    @Operation(description = "ENDPOINT responsável pelo delete de Role")
    @DeleteMapping("/RoleId/{RoleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long RoleId){
        role.deleteRole(RoleId);
        return ResponseEntity.noContent().build();
    }
}
