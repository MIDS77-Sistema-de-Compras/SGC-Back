package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.RequestRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.UpdateFeedback;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.RequestResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.useCases.serviceIntrf.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/requests")
@RestController
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RequestResponse> createRequest(@Valid @RequestBody RequestRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.createRequest(request));
    }

    @GetMapping
    public ResponseEntity<List<RequestResponse>> findAllRequest(){
        return ResponseEntity.ok(requestService.findAllRequest());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestResponse> findRequestById(@PathVariable Long id){
        return ResponseEntity.ok(requestService.findRequestById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestResponse> updateRequest(@Valid @RequestBody RequestRequest request, @PathVariable Long id){
        return ResponseEntity.ok(requestService.updateRequest(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id){
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RequestResponse> updateFeedback(@Valid @RequestBody UpdateFeedback feedback, @PathVariable Long id){
        return ResponseEntity.ok(requestService.updateFeedback(feedback, id));
    }
}
