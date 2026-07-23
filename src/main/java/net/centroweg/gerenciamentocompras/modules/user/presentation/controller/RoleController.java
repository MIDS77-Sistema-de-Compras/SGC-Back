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
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.Role;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de {@link Role}.
 */
@Tag(name = "ENDPOINTS da entidade nível de acesso")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleIntrf role;

    /**
     * Cria um novo nível de acesso.
     * @param roleRequest dados do nível de acesso.
     * @return nível de acesso criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um nível de acesso")
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRole roleRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(role.createRole(roleRequest));
    }

    /**
     * Lista todos os níveis de acesso cadastrados.
     * @return lista com todos os níveis de acesso encontrados, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos os níveis de acesso")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> listRole(){
        return ResponseEntity.ok(role.listRole());
    }

    /**
     * Busca um nível de acesso pelo seu identificador.
     * @param RoleId identificador do nível de acesso.
     * @return nível de acesso encontrado, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um nível de acesso por id")
    @GetMapping("/RoleId/{RoleId}")
    public ResponseEntity<RoleResponse> findRoleById(@PathVariable Long RoleId){
        return ResponseEntity.ok(role.findRoleById(RoleId));
    }

    /**
     * Busca um nível de acesso pelo seu nome.
     * @param RoleName nome do nível de acesso.
     * @return nível de acesso encontrado, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um nível de acesso pelo nome")
    @GetMapping("/RoleName/{RoleName}")
    public ResponseEntity<RoleResponse> findRoleByName(@PathVariable String RoleName){
        return ResponseEntity.ok(role.findRoleByName(RoleName));
    }

    /**
     * Atualiza um nível de acesso existente.
     * @param roleRequest novos dados do nível de acesso.
     * @param RoleId identificador do nível de acesso.
     * @return nível de acesso já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um nível de acesso")
    @PutMapping("/RoleId/{RoleId}")
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody CreateRole roleRequest, @PathVariable Long RoleId){
        return ResponseEntity.ok(role.updateRole(RoleId, roleRequest));
    }

    /**
     * Remove um nível de acesso.
     * @param RoleId identificador do nível de acesso.
     * @return resposta sem conteúdo confirmando a remoção.
     */
    @Operation(description = "ENDPOINT responsável por remover um nível de acesso")
    @DeleteMapping("/RoleId/{RoleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long RoleId){
        role.deleteRole(RoleId);
        return ResponseEntity.noContent().build();
    }
}
