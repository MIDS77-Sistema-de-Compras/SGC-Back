package net.centroweg.gerenciamentocompras.modules.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.UserIntrf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de {@link User}.
 */
@Tag(name = "ENDPOINTS da entidade usuário")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserIntrf user;

    /**
     * Cria um novo usuário.
     * @param userRequest dados do usuário.
     * @return usuário criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um usuário")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUser userRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(user.createUser(userRequest));
    }

    /**
     * Lista todos os usuários cadastrados.
     * @return lista com todos os usuários encontrados, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos os usuários")
    @GetMapping
    public ResponseEntity<List<UserResponse>> listUser(){
        return ResponseEntity.ok(user.listUser());
    }

    /**
     * Busca um usuário pelo seu identificador.
     * @param userId identificador do usuário.
     * @return usuário encontrado, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um usuário por id")
    @GetMapping("/userId/{userId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long userId){
        return ResponseEntity.ok(user.findUserById(userId));
    }

    /**
     * Busca os usuários pelo seu nome.
     * @param userName nome do usuário.
     * @return lista com todos os usuários encontrados, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um usuário pelo nome")
    @GetMapping("/userName/{userName}")
    public ResponseEntity<List<UserResponse>> findUserByName(@PathVariable String userName){
        return ResponseEntity.ok(user.findUserByName(userName));
    }

    /**
     * Atualiza um usuário existente.
     * @param userRequest novos dados do usuário.
     * @param userId identificador do usuário.
     * @return usuário já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um usuário")
    @PutMapping("/userId/{userId}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody CreateUser userRequest, @PathVariable Long userId){
        return ResponseEntity.ok(user.updateUserAll(userId, userRequest));
    }

    /**
     * Remove um usuário, tornando sua atividade inativa.
     * @param userId identificador do usuário.
     * @return resposta sem conteúdo confirmando a remoção.
     */
    @Operation(description = "ENDPOINT responsável por remover um usuário")
    @DeleteMapping("/userId/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        user.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza a foto de perfil de um usuário existente.
     * @param id identificador do usuário.
     * @param file arquivo da foto de perfil.
     * @return usuário já atualizado.
     * @throws IOException caso ocorra um erro durante o upload do arquivo.
     */
    @Operation(description = "ENDPOINT responsável pela edição da foto de perfil de um usuário")
    @PatchMapping("/userId/{id}")
    public ResponseEntity<UserResponse> updateProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(200).body(user.uploadProfilePicture(id, file));
    }

    /**
     * Busca o usuário autenticado.
     * @param userPrincipal dados do usuário autenticado.
     * @return usuário encontrado, caso exista.
     */
    @Operation(description = "ENDPOINT responsável por buscar o usuário que está logado")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> findLoggedUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.status(200).body(user.findLoggedUser(userPrincipal));
    }

}
