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
 * Controlador REST responsável pelos endpoints de gerenciamento de Sector.
 */
@Tag(name = "ENDPOINTS da entidade SECTOR")
@RestController
@RequestMapping("/sector")
@RequiredArgsConstructor
public class SectorController {

    private final SectorService service;

    /**
     * Criar um novo Sector.
     * @param sector corpo da requisição com dados.
     * @return status HTTP 201 com sector criado no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela criação de Sector")
    @PostMapping
    public ResponseEntity<SectorSimpleResponse> createSector(@Valid @RequestBody SectorRequest sector){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSector(sector));
    }

    /**
     * Listar todos os Sectors de forma simplificada.
     * @return status HTTP 200 com lista de sectors no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem simplificada de todos Sector")
    @GetMapping("/simple")
    public ResponseEntity<List<SectorSimpleResponse>> findAllSectorSimple(){
        return ResponseEntity.ok(service.findAllSectorSimple());
    }

    /**
     * Listar todos os Sectors de forma completa.
     * @return status HTTP 200 com lista de sectors no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem completa de todos Sector")
    @GetMapping("/compound")
    public ResponseEntity<List<SectorCompoundResponse>> findAllSectorCompound(){
        return ResponseEntity.ok(service.findAllSectorCompound());
    }

    /**
     * Buscar Sector de forma simplificada por identificador único.
     * @param id identificador do sector.
     * @return status HTTP 200 com sector encontrado no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem simplificada de Sector por id")
    @GetMapping("/simple/{id}")
    public ResponseEntity<SectorSimpleResponse> findSectorByIdSimple(@PathVariable Long id){
        return ResponseEntity.ok(service.findSectorByIdSimple(id));
    }

    /**
     * Buscar Sector de forma completa por identificador único
     * @param id identificador do sector.
     * @return status HTTP 200 com sector encontrado no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela listagem completa de Sector por id")
    @GetMapping("/compound/{id}")
    public ResponseEntity<SectorCompoundResponse> findSectorByIdCompound(@PathVariable Long id){
        return ResponseEntity.ok(service.findSectorByIdCompound(id));
    }

    /**
     * Atualizar Sector por identificador único.
     * @param sector corpo da requisição com novos dados.
     * @param id identificador do sector.
     * @return status HTTP 200 com sector atualizado no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pela atualização de Sector")
    @PutMapping("/{id}")
    public ResponseEntity<SectorSimpleResponse> updateSector(@Valid @RequestBody SectorRequest sector, @PathVariable Long id){
        return ResponseEntity.ok(service.updateSector(id, sector));
    }

    /**
     * Remover Sector por identificador único.
     * @param id identificador do sector.
     * @return status HTTP 200 e mesagem de confirmação no corpo da resposta.
     */
    @Operation(description = "ENDPOINT responsável pelo delete de Sector")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSector(@PathVariable Long id){
        service.deleteSector(id);
        return ResponseEntity.noContent().build();
    }
}
