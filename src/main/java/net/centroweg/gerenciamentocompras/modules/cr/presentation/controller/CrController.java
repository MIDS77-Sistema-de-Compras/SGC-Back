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
 * Controller REST responsável pelos endpoints de gerenciamento de Centros de Resultado (CR).
 * <p>
 * Expostos sob o caminho base {@code /cr}.
 * </p>
 */
@Tag(name = "ENDPOINTS da entidade CR")
@RestController
@RequestMapping("/cr")
@RequiredArgsConstructor
public class CrController {

    private final CrService crService;

    /**
     * Cria um novo Centro de Resultado.
     *
     * @param dto dados do CR a ser criado
     * @return {@link CrResponse} com os dados do CR criado e status HTTP 201
     */
    @Operation(description = "ENDPOINT responsável pela criação de CR")
    @PostMapping
    public ResponseEntity<CrCompoundResponse> create(@RequestBody CrRequest dto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crService.create(dto, userPrincipal));
    }

    /**
     * Lista todos os Centros de Resultado cadastrados.
     *
     * @return lista de {@link CrResponse} e status HTTP 200
     */
    @Operation(description = "ENDPOINT responsável pela listagem de todos CR")
    @GetMapping
    public ResponseEntity<List<CrCompoundResponse>> listAll(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.listAll());
    }

    /**
     * Busca um Centro de Resultado pelo seu identificador.
     *
     * @param id identificador do CR
     * @return {@link CrResponse} com os dados do CR e status HTTP 200
     */
    @Operation(description = "ENDPOINT responsável pela listagem de CR por id")
    @GetMapping("{id}")
    public ResponseEntity<CrCompoundResponse> listById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.listById(id));
    }

    /**
     * Atualiza os dados de um Centro de Resultado existente.
     *
     * @param id  identificador do CR a ser atualizado
     * @param dto novos dados do CR
     * @return {@link CrResponse} com os dados atualizados e status HTTP 200
     */
    @Operation(description = "ENDPOINT responsável pela atualização de CR")
    @PutMapping("{id}")
    public ResponseEntity<CrCompoundResponse> update(@PathVariable Long id, @RequestBody CrRequest dto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.update(id, dto));
    }

    /**
     * Remove um Centro de Resultado pelo seu identificador.
     *
     * @param id identificador do CR a ser removido
     * @return {@link MessageDTO} com mensagem de confirmação e status HTTP 200
     */
    @Operation(description = "ENDPOINT responsável pelo delete de CR")
    @DeleteMapping("{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.delete(id));
    }
}
