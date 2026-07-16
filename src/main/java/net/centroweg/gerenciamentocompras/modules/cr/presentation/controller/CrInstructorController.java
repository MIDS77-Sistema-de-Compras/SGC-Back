package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

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
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.crinstructorinterface.CrInstructorService;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de CR-instrutor.
 */
@RestController
@RequestMapping("/cr-instructors")
@RequiredArgsConstructor
@Tag(name = "ENDPOINTS da entidade CR-instrutor")
public class CrInstructorController {
    
    private final CrInstructorService crInstructorService;

    /**
     * Cria um novo vínculo entre CR e usuário.
     * @param request dados do vínculo.
     * @return vínculo criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um CR-instrutor")
    @PostMapping
    public ResponseEntity<CrInstructorResponse> create(@Valid @RequestBody CrInstructorRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(crInstructorService.create(request));
    }

    /**
     * Lista todos os vínculos CR-instrutores cadastrados.
     * @return lista com todos os vínculos CR-instrutores encontrados.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos os CR-instrutores")
    @GetMapping
    public ResponseEntity<List<CrInstructorResponse>> findAll(){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.findAll());
    }

    /**
     * Busca um vínculo CR-instrutor pelo seu identificador.
     * @param id identificador do vínculo.
     * @return vínculos encontrado.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR-instrutores por id")
    @GetMapping("/{id}")
    public ResponseEntity<CrInstructorResponse> findById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.findById(id));
    }

    /**
     * Atualiza um vínculo CR-instrutor existente.
     * @param id identificador do vínculo.
     * @param request novos dados do vínculo.
     * @return vínculo já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um CR-instrutor")
    @PutMapping("/{id}")
    public ResponseEntity<CrInstructorResponse> update(@PathVariable("id") Long id, @Valid @RequestBody CrInstructorRequest request){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.update(id, request));
    }

    /**
     * Remove um vínculo CR-instrutor.
     * @param id identificador do vínculo.
     * @return mensagem de confirmação da remoção.
     */
    @Operation(description = "ENDPOINT responsável por deletar os CR-instrutor")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.delete(id));
    }

}
