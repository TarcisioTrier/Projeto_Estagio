package triersistemas.estagio_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.services.ProdutoService;

import java.util.List;


@RestController
@RequestMapping("/produto")
public class ProdutoController {
    final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProdutoResponseDto> getProdutoById(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.getProdutoById(id));
    }

    @GetMapping("/getAllFilter")
    public Page<ProdutoResponseDto> getProdutoFilter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long grupoProdutoId,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) TipoProduto tipo) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoService.getProdutoFilter(nome, tipo, grupoProdutoId, pageable);
    }

    @GetMapping("/getAllProdutoAlteraPreco/")
    public List<ProdutoResponseDto> getAllProdutoAlteraPreco(){
        return produtoService.getAllProdutoAlteraPreco();
    }

    @PostMapping("/post")
    public ResponseEntity<ProdutoResponseDto> postProduto(@Valid @RequestBody ProdutoRequestDto produtoRequestDto) {
        return ResponseEntity.ok(produtoService.addProduto(produtoRequestDto));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<ProdutoResponseDto> putProduto(@PathVariable Long id,@Valid @RequestBody ProdutoRequestDto ProdutoRequestDto) {
        return ResponseEntity.ok(produtoService.updateProduto(id, ProdutoRequestDto));
    }

    @PutMapping("alteraPrecoProduto")
    public ResponseEntity<List<ProdutoResponseDto>> alteraPrecoProduto(@Valid @RequestBody AtualizaPrecoDto atualizaProduto){
        return ResponseEntity.ok(produtoService.alteraMargemProduto(atualizaProduto));
    }

    @DeleteMapping("/altera/{id}")
    public ResponseEntity<ProdutoResponseDto> alteraProdutoById(@PathVariable Long id, @RequestParam boolean ativar) {
        return ResponseEntity.ok(produtoService.alteraProdutoById(id, ativar));
    }
}
