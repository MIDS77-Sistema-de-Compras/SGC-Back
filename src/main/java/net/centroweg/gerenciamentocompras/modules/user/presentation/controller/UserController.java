package net.centroweg.gerenciamentocompras.modules.user.presentation.controller;

import java.io.IOException;
import java.util.List;

import net.centroweg.gerenciamentocompras.shared.audit.annotation.AuditParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.ChangePassword;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.UpdateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.UserIntrf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import net.centroweg.gerenciamentocompras.shared.audit.annotation.Auditable;

/**
 * Controller de usuários, gerencia criações, consultas, atualizações e remoção.
 */
@Tag(name = "ENDPOINTS da entidade USER")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * Injeção de dependência da interface de serviço dos usuários.
     * @see UserIntrf
     */

    private final UserIntrf user;

    /**
     * Cria um novo usuário no sistema.
     * @param userRequest dados do usuário que vai ser implementado no sistema
     * @return o usuário já criado com status {@code 201 Created}
     */
    @Operation(description = "ENDPOINT responsável pela criação de User")
    @PostMapping
    @Auditable(action = "CRIAR_USUARIO", targetFromReturn = true)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUser userRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(user.createUser(userRequest));
    }

    /**
     * Lista todos os usuários cadastrados.
     * @return lista de usuários com status {@code 200 OK}
     */

    @Operation(description = "ENDPOINT responsável pela listagem de todos User")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> listUser(Pageable pageable){
        return ResponseEntity.ok(user.listUser(pageable));
    }

    /**
     * Busca usuário pelo seu identificador único.
     * @return usuário encontrado com status {@code 200 OK}
     */

    @Operation(description = "ENDPOINT responsável pela listagem de User por id")
    @GetMapping("/userId/{userId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long userId){
        return ResponseEntity.ok(user.findUserById(userId));
    }

    /**
     * Busca usuário pelo seu nome.
     * @return usuário encontrado com status {@code 200 OK}
     */

    @Operation(description = "ENDPOINT responsável pela listagem de User por nome")
    @GetMapping("/userName/{userName}")
    public ResponseEntity<List<UserResponse>> findUserByName(@PathVariable String userName){
        return ResponseEntity.ok(user.findUserByName(userName));
    }

    /**
     * Atualiza usuário pelo seu identificador único.
     * @return usuário já atualizado com status {@code 200 OK}
     */

    @Operation(description = "ENDPOINT responsável pela atualização de User")
    @PutMapping("/userId/{userId}")
    @Auditable(action = "ATUALIZAR_USUARIO")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUser userRequest, @AuditParam(value = "user") @PathVariable Long userId){
        return ResponseEntity.ok(user.updateUserAll(userId, userRequest));
    }

    /**
     * Deleta usuário pelo seu identificador único, não o deletando completamente, apenas deixando sua atividade inativa.
     * @return resposta vazia com status {@code 204 No Content}
     */

    @Operation(description = "ENDPOINT responsável pelo delete de User")
    @DeleteMapping("/userId/{userId}")
    @Auditable(action = "DESATIVAR_USUARIO")
    public ResponseEntity<Void> deleteUser(@AuditParam(value = "user") @PathVariable Long userId){
        user.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "ENDPOINT responsável pela edição de foto de perfil")
    @PatchMapping("/userId/{id}")
    @Auditable(action = "ATUALIZAR_FOTO_DE_PERFIL")
    public ResponseEntity<UserResponse> updateProfilePicture(@AuditParam(value = "user") @PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(200).body(user.uploadProfilePicture(id, file));
    }

    @Operation(description = "ENDPOINT responsável por listar usuário logado")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findLoggedUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.status(200).body(user.findLoggedUser(userPrincipal));
    }

    @Operation(description = "ENDPOINT responsável por alterar a senha do usuário logado")
    @PostMapping("/me/change-password")
    @Auditable(action = "ATUALIZAR_SENHA")
    public ResponseEntity<MessageDTO> changeUserPassword(@AuditParam(value = "user") @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ChangePassword changePassword){
        Long userId = user.findLoggedUser(userPrincipal).id();
        return ResponseEntity.status(200).body(user.updatePwd(userId, changePassword));
    }

}
