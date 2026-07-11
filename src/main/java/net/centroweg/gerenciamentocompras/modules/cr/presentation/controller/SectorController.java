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
     * Criar um novo bloco.
     * @param sector corpo da requisição com dados.
     * @return bloco criado.
     */
    @Operation(description = "ENDPOINT responsável pela criação de bloco")
    @PostMapping
    public ResponseEntity<SectorSimpleResponse> createSector(@Valid @RequestBody SectorRequest sector){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSector(sector));
    }

    /**
     * Listar todos os blocos de forma simplificada.
     * @return lista de blocos encontrados.
     */
    @Operation(description = "ENDPOINT responsável pela listagem simplificada de todos blocos")
    @GetMapping("/simple")
    public ResponseEntity<List<SectorSimpleResponse>> findAllSectorSimple(){
        return ResponseEntity.ok(service.findAllSectorSimple());
    }

    /**
     * Listar todos os blocos de forma completa.
     * @return lista de blocos encontrados.
     */
    @Operation(description = "ENDPOINT responsável pela listagem completa de todos blocos")
    @GetMapping("/compound")
    public ResponseEntity<List<SectorCompoundResponse>> findAllSectorCompound(){
        return ResponseEntity.ok(service.findAllSectorCompound());
    }

    /**
     * Buscar bloco de forma simplificada por identificador único.
     * @param id identificador do bloco.
     * @return bloco encontrado.
     */
    @Operation(description = "ENDPOINT responsável pela listagem simplificada de bloco por id")
    @GetMapping("/simple/{id}")
    public ResponseEntity<SectorSimpleResponse> findSectorByIdSimple(@PathVariable Long id){
        return ResponseEntity.ok(service.findSectorByIdSimple(id));
    }

    /**
     * Buscar bloco de forma completa por identificador único
     * @param id identificador do bloco.
     * @return bloco encontrado.
     */
    @Operation(description = "ENDPOINT responsável pela listagem completa de bloco por id")
    @GetMapping("/compound/{id}")
    public ResponseEntity<SectorCompoundResponse> findSectorByIdCompound(@PathVariable Long id){
        return ResponseEntity.ok(service.findSectorByIdCompound(id));
    }

    /**
     * Atualizar bloco por identificador único.
     * @param sector corpo da requisição com novos dados.
     * @param id identificador do bloco.
     * @return bloco atualizado.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de bloco")
    @PutMapping("/{id}")
    public ResponseEntity<SectorSimpleResponse> updateSector(@Valid @RequestBody SectorRequest sector, @PathVariable Long id){
        return ResponseEntity.ok(service.updateSector(id, sector));
    }

    /**
     * Remover bloco por identificador único.
     * @param id identificador do bloco.
     * @return mensagem de confirmação da remoção.
     */
    @Operation(description = "ENDPOINT responsável por deletar o bloco")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSector(@PathVariable Long id){
        service.deleteSector(id);
        return ResponseEntity.noContent().build();
    }
}
