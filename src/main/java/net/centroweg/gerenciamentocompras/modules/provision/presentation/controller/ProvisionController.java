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
import net.centroweg.gerenciamentocompras.modules.provision.service.usecases.serviceintrf.ProvisionService;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de {@link Provision}.
 */
@Tag(name = "ENDPOINTS da entidade serviço")
@RestController
@RequestMapping("/provisions")
@RequiredArgsConstructor
public class ProvisionController {

    private final ProvisionService provisionService;

    /**
     * Cria um novo serviço.
     * @param request dados do serviço.
     * @return serviço criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um serviço")
    @PostMapping
    public ResponseEntity<ProvisionResponse> saveProvision(@Valid @RequestBody ProvisionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(provisionService.createProvision(request));
    }

    /**
     * Lista todos os serviços cadastrados com o nome informado.
     * @return lista com todos os serviços encontrados, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos os serviços")
    @GetMapping
    public ResponseEntity<List<ProvisionResponse>> listAllProvision(){
        return ResponseEntity.status(HttpStatus.OK)
            .body(provisionService.getAllProvisions());
    }

    /**
     * Busca um serviço pelo seu identificador.
     * @param id identificador do serviço.
     * @return serviço encontrado, caso exista.
     */
    @Operation(description = "ENDPOINT responsável pela busca de um serviço por id")
    @GetMapping("/{id}")
    public ResponseEntity<ProvisionResponse> listProvisionById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(provisionService.getProvisionById(id));
    }

    /**
     * Atualiza um serviço existente.
     * @param id identificador do serviço.
     * @param request novos dados do serviço.
     * @return serviço já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um serviço")
    @PutMapping("/{id}")
    public ResponseEntity<ProvisionResponse> updateProvision(@PathVariable("id") Long id, @Valid @RequestBody ProvisionRequest request){
        return ResponseEntity.status(HttpStatus.OK)
            .body(provisionService.updateProvision(id, request));
    }

    /**
     * Remove um serviço.
     * @param id identificador do serviço.
     * @return resposta sem conteúdo confirmando a remoção.
     */
    @Operation(description = "ENDPOINT responsável por remover um serviço")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvision(@PathVariable("id") Long id){
        provisionService.deleteProvision(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
