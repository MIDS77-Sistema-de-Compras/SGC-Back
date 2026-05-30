package net.centroweg.gerenciamentocompras.modules.user.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.UserIntrf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller de usuários, gerencia criações, consultas, atualizações e remoção.
 */

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
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUser userRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(user.createUser(userRequest));
    }

    /**
     * Lista todos os usuários cadastrados.
     * @return lista de usuários com status {@code 200 OK}
     */

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUser(){
        return ResponseEntity.ok(user.listUser());
    }

    /**
     * Busca usuário pelo seu identificador único.
     * @return usuário encontrado com status {@code 200 OK}
     */

    @GetMapping("/UserId/{UserId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long UserId){
        return ResponseEntity.ok(user.findUserById(UserId));
    }

    /**
     * Busca usuário pelo seu nome.
     * @return usuário encontrado com status {@code 200 OK}
     */

    @GetMapping("/UserName/{UserName}")
    public ResponseEntity<List<UserResponse>> findUserByName(@PathVariable String UserName){
        return ResponseEntity.ok(user.findUserByName(UserName));
    }

    /**
     * Atualiza usuário pelo seu identificador único.
     * @return usuário já atualizado com status {@code 200 OK}
     */

    @PutMapping("/UserId/{UserId}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody CreateUser userRequest, @PathVariable Long UserId){
        return ResponseEntity.ok(user.updateUserAll(UserId, userRequest));
    }

    /**
     * Deleta usuário pelo seu identificador único, não o deletando completamente, apenas deixando sua atividade inativa.
     * @return resposta vazia com status {@code 204 No Content}
     */

    @DeleteMapping("/UserId/{UserId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long UserId){
        user.deleteUser(UserId);
        return ResponseEntity.noContent().build();
    }
}
