package net.centroweg.gerenciamentocompras.modules.cr.presentation.controller;

import lombok.RequiredArgsConstructor;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.request.CrRequest;
import net.centroweg.gerenciamentocompras.modules.cr.presentation.dto.response.CrResponse;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.CrService;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.CrServiceImpl;
import net.centroweg.gerenciamentocompras.modules.cr.service.crservice.CreateCr;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cr")
@RequiredArgsConstructor
public class CrController {

    private final CrServiceImpl CrService;

    @PostMapping
    public ResponseEntity<CrResponse> create(@RequestBody CrRequest dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CrService.create(dto));
    }
}
