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
 * Controlador REST responsável pelos endpoints de gerenciamento de Centros de Responsabilidade(CR).
 */
@Tag(name = "ENDPOINTS da entidade CR")
@RestController
@RequestMapping("/cr")
@RequiredArgsConstructor
public class CrController {

    private final CrService crService;

    /**
     * Criar um novo CR.
     * @param dto corpo da requisição com os dados de criação.
     * @return status HTTP 201 e CR criado no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela criação de CR")
    @PostMapping
    public ResponseEntity<CrCompoundResponse> create(@RequestBody CrRequest dto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crService.create(dto, userPrincipal));
    }

    /**
     * Listar todos os CRs cadastrados.
     * @return status HTTP 200 e uma lista de todos os CRs encontrados no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos CR")
    @GetMapping
    public ResponseEntity<List<CrCompoundResponse>> listAll(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.listAll());
    }

    /**
     * Buscar um CR pelo seu identificador.
     * @param id identificador do CR para pesquisa.
     * @return status HTTP 200 e CR encontrado no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR por id")
    @GetMapping("{id}")
    public ResponseEntity<CrCompoundResponse> listById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.listById(id));
    }

    /**
     * Atualizar os dados de um CR existente.
     * @param id  identificador do CR a ser atualizado.
     * @param dto requisição com novos dados.
     * @return status HTTP 200 e CR com dados atualizados no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de CR")
    @PutMapping("{id}")
    public ResponseEntity<CrCompoundResponse> update(@PathVariable Long id, @RequestBody CrRequest dto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.update(id, dto));
    }

    /**
     * Remover um CR pelo seu identificador.
     * @param id identificador do CR a ser removido.
     * @return status HTTP 200 e mesagem de confirmação no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pelo delete de CR")
    @DeleteMapping("{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.delete(id));
    }
}
