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

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserIntrf user;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUser userRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(user.createUser(userRequest));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUser(){
        return ResponseEntity.ok(user.listUser());
    }

    @GetMapping("/UserId/{UserId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long UserId){
        return ResponseEntity.ok(user.findUserById(UserId));
    }

    @GetMapping("/UserName/{UserName}")
    public ResponseEntity<List<UserResponse>> findUserByName(@PathVariable String UserName){
        return ResponseEntity.ok(user.findUserByName(UserName));
    }

    @PutMapping("/UserId/{UserId}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody CreateUser userRequest, @PathVariable Long UserId){
        return ResponseEntity.ok(user.updateUserAll(UserId, userRequest));
    }

    @DeleteMapping("/UserId/{UserId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long UserId){
        user.deleteUser(UserId);
        return ResponseEntity.noContent().build();
    }
}
