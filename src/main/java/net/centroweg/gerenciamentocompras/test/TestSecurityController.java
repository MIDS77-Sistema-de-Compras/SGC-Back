package net.centroweg.gerenciamentocompras.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testroute")
public class TestSecurityController {

    @GetMapping("/public")
    public ResponseEntity<String> testRoute(){
        return ResponseEntity.status(200)
                .body("Página acessada com sucesso!");
    }
}
