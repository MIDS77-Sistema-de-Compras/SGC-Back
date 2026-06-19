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

/**
 * Classe responsável por controlar os endpoints da API
 * @author gabrielEFagundes
 * @version 0.1.0
 * @see ProvisionService
 * @see ProvisionRequest
 * @see ProvisionResponse
 */
@Tag(name = "ENDPOINTS da entidade PROVISION")
@RestController
@RequestMapping("/provisions")
@RequiredArgsConstructor
public class ProvisionController {

    private final ProvisionService provisionService;

    /**
     * Método responsável por salvar o serviço no banco de dados.
     * @param request O corpo da requisição inserido pelo usuário.
     * @return {@code ResponseEntity<ProvisionResponse>} Uma resposta de status HTTP com o corpo do serviço salvo.
     * @see ProvisionService#createProvision(ProvisionRequest)
     */
    @Operation(description = "ENDPOINT responsável pela criação de Provision")
    @PostMapping
    public ResponseEntity<ProvisionResponse> saveProvision(@Valid @RequestBody ProvisionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(provisionService.createProvision(request));
    }

    /**
     * Método responsável por buscar todos os serviços no banco de dados.
     * @return {@code ResponseEntity<List<ProvisionResponse>>} Uma resposta de status HTTP com a lista de serviços.
     * @see ProvisionService#getAllProvisions()
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos Provision")
    @GetMapping
    public ResponseEntity<List<ProvisionResponse>> listAllProvision(){
        return ResponseEntity.status(HttpStatus.OK)
            .body(provisionService.getAllProvisions());
    }

    /**
     * Método responsável por buscar o serviço correspondente com o ID no banco de dados.
     * @param id O ID do serviço desejado.
     * @return {@code ResponseEntity<ProvisionResponse>} Uma resposta de status HTTP com o serviço encontrado.
     * @see ProvisionService#getProvisionById(Long)
     */
    @Operation(description = "ENDPOINT responsável pela listagem de Provision por id")
    @GetMapping("/{id}")
    public ResponseEntity<ProvisionResponse> listProvisionById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(provisionService.getProvisionById(id));
    }

    /**
     * Método responsável por atualizar o serviço correspondente com o ID no banco de dados.
     * @param id O ID do serviço desejado.
     * @param request O corpo da requisição inserido pelo usuário.
     * @return {@code ResponseEntity<ProvisionResponse>} Uma resposta de status HTTP com o corpo do serviço atualizado.
     * @see ProvisionService#updateProvision(Long, ProvisionRequest)
     */
    @Operation(description = "ENDPOINT responsável pela atualização de Provision")
    @PutMapping("/{id}")
    public ResponseEntity<ProvisionResponse> updateProvision(@PathVariable("id") Long id, @Valid @RequestBody ProvisionRequest request){
        return ResponseEntity.status(HttpStatus.OK)
            .body(provisionService.updateProvision(id, request));
    }

    /**
     * Método responsável por deletar o serviço correspondente com o ID no banco de dados.
     * @param id O ID do serviço desejado.
     * @return {@code ResponseEntity<Void>} Uma resposta de status HTTP sem corpo.
     * @see ProvisionService#deleteProvision(Long)
     */
    @Operation(description = "ENDPOINT responsável pelo delete de Provision")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvision(@PathVariable("id") Long id){
        provisionService.deleteProvision(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
