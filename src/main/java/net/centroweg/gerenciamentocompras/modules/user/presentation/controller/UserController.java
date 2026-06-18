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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @GetMapping("/userId/{userId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long userId){
        return ResponseEntity.ok(user.findUserById(userId));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de User por nome")
    @GetMapping("/userName/{userName}")
    public ResponseEntity<List<UserResponse>> findUserByName(@PathVariable String userName){
        return ResponseEntity.ok(user.findUserByName(userName));
    }

    @Operation(description = "ENDPOINT responsável pela atualização de User")
    @PutMapping("/userId/{userId}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody CreateUser userRequest, @PathVariable Long userId){
        return ResponseEntity.ok(user.updateUserAll(userId, userRequest));
    }

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


}
