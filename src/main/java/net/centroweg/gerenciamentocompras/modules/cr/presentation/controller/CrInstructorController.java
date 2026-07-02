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
     * Criar um novo CR-instrutor.
     * @param request corpo da requisição com dados de criação do CR-instrutor.
     * @return status HTTP 201 e CR-instrutor criado no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela criação de CR-instrutor")
    @PostMapping
    public ResponseEntity<CrInstructorResponse> create(@Valid @RequestBody CrInstructorRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(crInstructorService.create(request));
    }

    /**
     * Listar CR-instrutors cadastrados.
     * @return status HTTP 200 e uma lista com todos os CR-instrutor encontrados no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos CR-instrutor")
    @GetMapping
    public ResponseEntity<List<CrInstructorResponse>> findAll(){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.findAll());
    }

    /**
     * Buscar CR-instrutor por identificador único.
     * @param id identificador do CR-instrutor para pesquisa.
     * @return status HTTP 200 com CR-instrutor encontrado no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR-instrutor por id")
    @GetMapping("/{id}")
    public ResponseEntity<CrInstructorResponse> findById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.findById(id));
    }

    /**
     * Atualizar CR-instrutor pelo identificador único.
     * @param id identificador do CR-instrutor para atualização.
     * @param request corpo de requisição com novos dados.
     * @return status HTTP 200 com CR-instrutor atualizado no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de CR-instrutor")
    @PutMapping("/{id}")
    public ResponseEntity<CrInstructorResponse> update(@PathVariable("id") Long id, @Valid @RequestBody CrInstructorRequest request){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.update(id, request));
    }

    /**
     * Remover CR-instrutor pelo identificador único.
     * @param id identificador do CR-instrutor para remoção.
     * @return status HTTP 200 e mensagem de confirmação no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável por deletar os CR-instrutor")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.delete(id));
    }

}
