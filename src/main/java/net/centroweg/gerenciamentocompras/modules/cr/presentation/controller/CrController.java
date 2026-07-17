package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.auth.domain.entity.UserPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface.CrService;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento do Centro de Responsabilidade(CR).
 */
@Tag(name = "ENDPOINTS da entidade CR")
@RestController
@RequestMapping("/cr")
@RequiredArgsConstructor
public class CrController {

    private final CrService crService;

    /**
     * Cria um novo CR.
     * @param dto dados do CR.
     * @return CR criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um CR")
    @PostMapping
    public ResponseEntity<CrCompoundResponse> create(@RequestBody CrRequest dto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crService.create(dto, userPrincipal));
    }

    /**
     * Lista todos os CRs cadastrados.
     * @return lista com todos os CRs encontrados.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos os CR")
    @GetMapping
    public ResponseEntity<List<CrCompoundResponse>> listAll(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.listAll());
    }

    /**
     * Busca um CR pelo seu identificador.
     * @param id identificador do CR.
     * @return CR encontrado.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR por id")
    @GetMapping("{id}")
    public ResponseEntity<CrCompoundResponse> listById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.listById(id));
    }

    /**
     * Atualiza um CR existente.
     * @param id identificador do CR.
     * @param dto novos dados do CR.
     * @return CR já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um CR")
    @PutMapping("{id}")
    public ResponseEntity<CrCompoundResponse> update(@PathVariable Long id, @RequestBody CrRequest dto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.update(id, dto));
    }

    /**
     * Remove um CR.
     * @param id identificador do CR.
     * @return mensagem de confirmação da remoção.
     */
    @Operation(description = "ENDPOINT responsável por remover um CR")
    @DeleteMapping("{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.delete(id));
    }
}
