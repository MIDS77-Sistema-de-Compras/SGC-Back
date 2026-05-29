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

/** Endpoints relacionados aos niveis de acesso do usuário */

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleIntrf role;

    /**
     * Cria uma nova role no sistema
     * @param roleRequest DTO de criação de role
     * @return status 201 - criado com sucesso, junto com DTO de reponse
     * @see CreateRole
     * @see RoleResponse
     */
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRole roleRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(role.createRole(roleRequest));
    }

    /**
     * Lista todas as roles sem filtros
     * @return uma lista com todas as roles
     * @see RoleResponse
     */
    @GetMapping
    public ResponseEntity<List<RoleResponse>> listRole(){
        return ResponseEntity.ok(role.listRole());
    }

    /**
     * Retorna uma role específica com base no ID
     * @param RoleId ID da role requisitada
     * @return role com o ID correspondente
     * @see RoleResponse
     */
    @GetMapping("/RoleId/{RoleId}")
    public ResponseEntity<RoleResponse> findRoleById(@PathVariable Long RoleId){
        return ResponseEntity.ok(role.findRoleById(RoleId));
    }

    /**
     * Lista roles contendo o nome pesquisado
     * @param RoleName nome pesquisado
     * @return uma lista com todos os nomes correspondentes
     * @see RoleResponse
     */
    @GetMapping("/RoleName/{RoleName}")
    public ResponseEntity<List<RoleResponse>> findRoleByName(@PathVariable String RoleName){
        return ResponseEntity.ok(role.findRoleByName(RoleName));
    }

    /**
     * Atualiza uma role específica no sistema
     * @param roleRequest novas informações para realizar a alteração
     * @param RoleId ID da role a ser modificada
     * @return status 200 com o objeto modificado
     * @see RoleResponse
     * @see CreateRole
     */
    @PutMapping("/RoleId/{RoleId}")
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody CreateRole roleRequest, @PathVariable Long RoleId){
        return ResponseEntity.ok(role.updateRole(RoleId, roleRequest));
    }

    /**
     * Exclui role específica do sistema
     * @param RoleId ID da role a ser excluída
     * @return status 204
     */
    @DeleteMapping("/RoleId/{RoleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long RoleId){
        role.deleteRole(RoleId);
        return ResponseEntity.noContent().build();
    }
}
