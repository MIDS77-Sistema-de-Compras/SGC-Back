package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrInstructorRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrInstructorResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crinstructor.crinstructorinterface.CrInstructorService;
import net.centroweg.gerenciamentocompras.shared.MessageDTO;

@RestController
@RequestMapping("/cr-instructors")
@RequiredArgsConstructor
public class CrInstructorController {
    
    private final CrInstructorService crInstructorService;

    @PostMapping
    public ResponseEntity<CrInstructorResponse> create(@Valid @RequestBody CrInstructorRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(crInstructorService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CrInstructorResponse>> findAll(){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrInstructorResponse> findById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CrInstructorResponse> update(@PathVariable("id") Long id, @Valid @RequestBody CrInstructorRequest request){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.delete(id));
    }

}
