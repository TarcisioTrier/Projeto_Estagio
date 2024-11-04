package triersistemas.estagio_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.services.ProdutoService;


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

    @PutMapping("/getAllPaged")
    public Page<ProdutoResponseDto> getProdutoPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long filialId,
            @RequestBody(required = false) ProdutoPagedRequestDto ProdutoPagedDto){
        Pageable pageable = PageRequest.of(page, size);
        return produtoService.getProdutoPaged(ProdutoPagedDto, filialId, pageable);
    }


    @PostMapping("/post")
    public ResponseEntity<ProdutoResponseDto> postProduto(@Valid @RequestBody ProdutoRequestDto produtoDto) {
        return ResponseEntity.ok(produtoService.addProduto(produtoDto));
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<ProdutoResponseDto> putProduto(@PathVariable Long id,@Valid @RequestBody ProdutoRequestDto produtoDto) {
        return ResponseEntity.ok(produtoService.updateProduto(id, produtoDto));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ProdutoResponseDto> removeProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.removeProduto(id));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ProdutoResponseDto> deleteProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.deleteProduto(id));
    }
}
