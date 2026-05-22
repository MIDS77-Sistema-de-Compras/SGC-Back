package net.centroweg.gerenciamentocompras.modules.request.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request.StatusRequest;
import net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response.StatusResponse;
import net.centroweg.gerenciamentocompras.modules.request.service.status.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/status")
@RestController
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @PostMapping
    public ResponseEntity<StatusResponse> addStatus(@Valid @RequestBody StatusRequest statusRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(statusService.createStatus(statusRequest));
    }

    @GetMapping
    public ResponseEntity<List<StatusResponse>> listStatus () {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.findAllStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusResponse> findStatusById (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.findStatusById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusResponse> editStatus (@PathVariable Long id, @Valid @RequestBody StatusRequest statusRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(statusService.editStatus(id, statusRequest));
    }
}