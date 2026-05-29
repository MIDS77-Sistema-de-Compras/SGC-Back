package net.centroweg.gerenciamentocompras.modules.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.request.CreateUser;
import net.centroweg.gerenciamentocompras.modules.user.presentation.dto.response.UserResponse;
import net.centroweg.gerenciamentocompras.modules.user.service.usecases.serviceIntrf.UserIntrf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ENDPOINTS da entidade USER")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserIntrf user;

    @Operation(description = "ENDPOINT responsável pela criação de User")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUser userRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(user.createUser(userRequest));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de todos User")
    @GetMapping
    public ResponseEntity<List<UserResponse>> listUser(){
        return ResponseEntity.ok(user.listUser());
    }

    @Operation(description = "ENDPOINT responsável pela listagem de User por id")
    @GetMapping("/UserId/{UserId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long UserId){
        return ResponseEntity.ok(user.findUserById(UserId));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de User por nome")
    @GetMapping("/UserName/{UserName}")
    public ResponseEntity<List<UserResponse>> findUserByName(@PathVariable String UserName){
        return ResponseEntity.ok(user.findUserByName(UserName));
    }

    @Operation(description = "ENDPOINT responsável pela atualização de User")
    @PutMapping("/UserId/{UserId}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody CreateUser userRequest, @PathVariable Long UserId){
        return ResponseEntity.ok(user.updateUserAll(UserId, userRequest));
    }

    @Operation(description = "ENDPOINT responsável pelo delete de User")
    @DeleteMapping("/UserId/{UserId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long UserId){
        user.deleteUser(UserId);
        return ResponseEntity.noContent().build();
    }
}
