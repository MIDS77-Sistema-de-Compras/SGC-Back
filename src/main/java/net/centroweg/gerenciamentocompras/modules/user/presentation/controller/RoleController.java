package net.centroweg.gerenciamentocompras.modules.user.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateRole;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.RoleResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.RoleIntrf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleIntrf role;

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRole roleRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(role.createRole(roleRequest));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> listRole(){
        return ResponseEntity.ok(role.listRole());
    }

    @GetMapping("/RoleId/{RoleId}")
    public ResponseEntity<RoleResponse> findRoleById(@PathVariable Long RoleId){
        return ResponseEntity.ok(role.findRoleById(RoleId));
    }

    @GetMapping("/RoleName/{RoleName}")
    public ResponseEntity<List<RoleResponse>> findRoleByName(@PathVariable String RoleName){
        return ResponseEntity.ok(role.findRoleByName(RoleName));
    }

    @PutMapping("/RoleId/{RoleId}")
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody CreateRole roleRequest, @PathVariable Long RoleId){
        return ResponseEntity.ok(role.updateRole(RoleId, roleRequest));
    }

    @DeleteMapping("/RoleId/{RoleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long RoleId){
        role.deleteRole(RoleId);
        return ResponseEntity.noContent().build();
    }
}
