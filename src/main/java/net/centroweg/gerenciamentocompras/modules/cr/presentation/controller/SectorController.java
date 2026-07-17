package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.SectorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorCompoundResponse;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.SectorSimpleResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.sectorservice.sectorinterface.SectorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST responsável pelos endpoints de gerenciamento de bloco.
 */
@Tag(name = "ENDPOINTS da entidade bloco")
@RestController
@RequestMapping("/bloco")
@RequiredArgsConstructor
public class SectorController {

    private final SectorService service;

    /**
     * Cria um novo bloco.
     * @param sector dados do bloco.
     * @return bloco criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de um bloco")
    @PostMapping
    public ResponseEntity<SectorSimpleResponse> createSector(@Valid @RequestBody SectorRequest sector){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSector(sector));
    }

    /**
     * Lista todos os blocos cadastrados de forma simples.
     * @return lista de todos os blocos encontrados.
     */
    @Operation(description = "ENDPOINT responsável pela listagem simples de todos os blocos")
    @GetMapping("/simple")
    public ResponseEntity<List<SectorSimpleResponse>> findAllSectorSimple(){
        return ResponseEntity.ok(service.findAllSectorSimple());
    }

    /**
     * Lista todos os blocos cadastrados de forma completa.
     * @return lista de todos os blocos encontrados.
     */
    @Operation(description = "ENDPOINT responsável pela listagem completa de todos os blocos")
    @GetMapping("/compound")
    public ResponseEntity<List<SectorCompoundResponse>> findAllSectorCompound(){
        return ResponseEntity.ok(service.findAllSectorCompound());
    }

    /**
     * Busca um  bloco de forma simples pelo seu identificador.
     * @param id identificador do bloco.
     * @return bloco encontrado.
     */
    @Operation(description = "ENDPOINT responsável pela listagem simples de bloco por id")
    @GetMapping("/simple/{id}")
    public ResponseEntity<SectorSimpleResponse> findSectorByIdSimple(@PathVariable Long id){
        return ResponseEntity.ok(service.findSectorByIdSimple(id));
    }

    /**
     * Busca um bloco de forma completa por identificador.
     * @param id identificador do bloco.
     * @return bloco encontrado.
     */
    @Operation(description = "ENDPOINT responsável pela listagem completa de bloco por id")
    @GetMapping("/compound/{id}")
    public ResponseEntity<SectorCompoundResponse> findSectorByIdCompound(@PathVariable Long id){
        return ResponseEntity.ok(service.findSectorByIdCompound(id));
    }

    /**
     * Atualiza um bloco existente.
     * @param sector novos dados do bloco.
     * @param id identificador do bloco.
     * @return bloco já atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de um bloco")
    @PutMapping("/{id}")
    public ResponseEntity<SectorSimpleResponse> updateSector(@Valid @RequestBody SectorRequest sector, @PathVariable Long id){
        return ResponseEntity.ok(service.updateSector(id, sector));
    }

    /**
     * Remove um bloco.
     * @param id identificador do bloco.
     * @return mensagem de confirmação da remoção.
     */
    @Operation(description = "ENDPOINT responsável por remover um bloco")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSector(@PathVariable Long id){
        service.deleteSector(id);
        return ResponseEntity.noContent().build();
    }
}
