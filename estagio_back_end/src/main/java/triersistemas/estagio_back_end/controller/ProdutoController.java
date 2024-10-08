package triersistemas.estagio_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.services.ProdutoService;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
    final ProdutoService ProdutoService;

    public ProdutoController(ProdutoService ProdutoService) {
        this.ProdutoService = ProdutoService;
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<ProdutoResponseDto> getProdutoById(@PathVariable Long id) {
        return ResponseEntity.ok(ProdutoService.getProdutoById(id));
    }

    @GetMapping("/getAllFilter")
    public Page<ProdutoResponseDto> getProdutoFilter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long grupoProdutoId,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) TipoProduto tipo) {
        Pageable pageable = PageRequest.of(page, size);
        return ProdutoService.getProdutoFilter(nome, tipo, grupoProdutoId, pageable);
    }

    @PostMapping("/post")
    public ResponseEntity<ProdutoResponseDto> postProduto(@Valid @RequestBody ProdutoRequestDto ProdutoRequestDto) {
        return ResponseEntity.ok(ProdutoService.addProduto(ProdutoRequestDto));
    }
    @PutMapping("/put/{id}")
    public ResponseEntity<ProdutoResponseDto> putProduto(@PathVariable Long id,@Valid @RequestBody ProdutoRequestDto ProdutoRequestDto) {
        return ResponseEntity.ok(ProdutoService.updateProduto(id, ProdutoRequestDto));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ProdutoResponseDto> deleteProdutoById(@PathVariable Long id) {
        return ResponseEntity.ok(ProdutoService.deleteProdutoById(id));
    }
    @DeleteMapping("/altera/{id}")
    public ResponseEntity<ProdutoResponseDto> alteraProdutoById(@PathVariable Long id, @RequestParam boolean ativar) {
        return ResponseEntity.ok(ProdutoService.alteraProdutoById(id, ativar));
    }
}
