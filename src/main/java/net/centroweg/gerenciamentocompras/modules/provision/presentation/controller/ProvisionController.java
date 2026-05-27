package net.centroweg.gerenciamentocompras.modules.provision.presentation.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.request.ProvisionRequest;
import net.centroweg.gerenciamentocompras.modules.provision.presentation.dto.response.ProvisionResponse;
import net.centroweg.gerenciamentocompras.modules.provision.service.interfaces.ProvisionService;

@Tag(name = "ENDPOINTS da entidade PROVISION")
@RestController
@RequestMapping("/provisions")
@RequiredArgsConstructor
public class ProvisionController {

    private final ProvisionService provisionService;

    @Operation(description = "ENDPOINT responsável pela criação de Provision")
    @PostMapping
    public ResponseEntity<ProvisionResponse> saveProvision(@Valid @RequestBody ProvisionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(provisionService.createProvision(request));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de todos Provision")
    @GetMapping
    public ResponseEntity<List<ProvisionResponse>> listAllProvision(){
        return ResponseEntity.status(HttpStatus.OK)
            .body(provisionService.getAllProvisions());
    }

    @Operation(description = "ENDPOINT responsável pela listagem de Provision por id")
    @GetMapping("/{id}")
    public ResponseEntity<ProvisionResponse> listProvisionById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(provisionService.getProvisionById(id));
    }

    @Operation(description = "ENDPOINT responsável pela atualização de Provision")
    @PutMapping("/{id}")
    public ResponseEntity<ProvisionResponse> updateProvision(@PathVariable("id") Long id, @Valid @RequestBody ProvisionRequest request){
        return ResponseEntity.status(HttpStatus.OK)
            .body(provisionService.updateProvision(id, request));
    }

    @Operation(description = "ENDPOINT responsável pelo delete de Provision")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvision(@PathVariable("id") Long id){
        provisionService.deleteProvision(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
