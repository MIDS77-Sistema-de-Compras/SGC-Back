package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.crinterface.CrService;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cr")
@RequiredArgsConstructor
public class CrController {

    private final CrService crService;

    @PostMapping
    public ResponseEntity<CrResponse> create(@RequestBody CrRequest dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<CrResponse>> listAll(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.listAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<CrResponse> listById(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.listById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<CrResponse> update(@PathVariable long id, @RequestBody CrRequest dto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.update(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(crService.delete(id));
    }
}
