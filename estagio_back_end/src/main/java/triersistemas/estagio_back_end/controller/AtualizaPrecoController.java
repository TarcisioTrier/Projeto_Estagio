package triersistemas.estagio_back_end.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.services.AtualizaPrecoService;

import java.util.List;

@RestController
@RequestMapping("/atualiza")
public class AtualizaPrecoController {

    private final AtualizaPrecoService service;

    public AtualizaPrecoController(AtualizaPrecoService service) {
        this.service = service;
    }

    @PutMapping("/post")
    public ResponseEntity<?> AtualizaPreco(@RequestBody AtualizaPrecoDto dto) {
        return ResponseEntity.ok(service.atualizaPreco(dto));
    }

}
