package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController
@RequestMapping("/cr-instructors")
@RequiredArgsConstructor
@Tag(name = "ENDPOINTS da entidade CR Instructor")
public class CrInstructorController {
    
    private final CrInstructorService crInstructorService;

    @Operation(description = "ENDPOINT responsável pela criação de CR Instrutor")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<CrInstructorResponse> create(@Valid @RequestBody CrInstructorRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(crInstructorService.create(request));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de todos CR Instructor")
    @GetMapping
    public ResponseEntity<Page<CrInstructorResponse>> findAll(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.findAll(pageable));
    }

    @Operation(description = "ENDPOINT responsável pela listagem de CR Instructor por id")
    @GetMapping("/{id}")
    public ResponseEntity<CrInstructorResponse> findById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.findById(id));
    }

    @Operation(description = "ENDPOINT responsável pela atualização de CR Instructor")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CrInstructorResponse> update(@PathVariable("id") Long id, @Valid @RequestBody CrInstructorRequest request){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.update(id, request));
    }

    @Operation(description = "ENDPOINT responsável pelo delete de CR Instructor")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> delete(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
            .body(crInstructorService.delete(id));
    }

}
