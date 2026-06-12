package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

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

@RestController
@RequestMapping("/sector")
@RequiredArgsConstructor
public class SectorController {

    private final SectorService service;

    @PostMapping
    public ResponseEntity<SectorSimpleResponse> createSector(@Valid @RequestBody SectorRequest sector){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSector(sector));
    }

    @GetMapping("/simple")
    public ResponseEntity<List<SectorSimpleResponse>> findAllSectorSimple(){
        return ResponseEntity.ok(service.findAllSectorSimple());
    }

    @GetMapping("/compound")
    public ResponseEntity<List<SectorCompoundResponse>> findAllSectorCompound(){
        return ResponseEntity.ok(service.findAllSectorCompound());
    }

    @GetMapping("/simple/{id}")
    public ResponseEntity<SectorSimpleResponse> findSectorByIdSimple(@PathVariable Long id){
        return ResponseEntity.ok(service.findSectorByIdSimple(id));
    }

    @GetMapping("/compound/{id}")
    public ResponseEntity<SectorCompoundResponse> findSectorByIdCompound(@PathVariable Long id){
        return ResponseEntity.ok(service.findSectorByIdCompound(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectorSimpleResponse> updateSector(@Valid @RequestBody SectorRequest sector, @PathVariable Long id){
        return ResponseEntity.ok(service.updateSector(id, sector));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSector(@PathVariable Long id){
        service.deleteSector(id);
        return ResponseEntity.noContent().build();
    }
}
