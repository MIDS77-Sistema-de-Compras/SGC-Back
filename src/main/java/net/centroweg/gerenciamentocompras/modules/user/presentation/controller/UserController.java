package net.centroweg.gerenciamentocompras.modules.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.ChangePassword;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
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
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody CreateUser userRequest, @PathVariable Long userId){
        return ResponseEntity.ok(user.updateUserAll(userId, userRequest));
    }

    /**
     * Deleta usuário pelo seu identificador único, não o deletando completamente, apenas deixando sua atividade inativa.
     * @return resposta vazia com status {@code 204 No Content}
     */

    @Operation(description = "ENDPOINT responsável pelo delete de User")
    @DeleteMapping("/userId/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        user.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "ENDPOINT responsável pela edição de foto de perfil")
    @PatchMapping("/userId/{id}")
    public ResponseEntity<UserResponse> updateProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(200).body(user.uploadProfilePicture(id, file));
    }

    @Operation(description = "ENDPOINT responsável por listar usuário logado")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findLoggedUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.status(200).body(user.findLoggedUser(userPrincipal));
    }

}
