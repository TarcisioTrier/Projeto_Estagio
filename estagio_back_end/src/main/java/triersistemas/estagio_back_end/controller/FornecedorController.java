package triersistemas.estagio_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.services.FornecedorService;

@RestController
@RequestMapping("fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping("/post")
    public ResponseEntity<FornecedorResponseDto> postFornecedor(@Valid @RequestBody FornecedorRequestDto requestDto) {
        return ResponseEntity.ok(fornecedorService.addFornecedor(requestDto));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<FornecedorResponseDto> getFornecedorById(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.getFornecedorById(id));
    }

    @GetMapping("/getAllFilter")
    public Page<FornecedorResponseDto> getFilialFilter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Valid @RequestParam(required = false) String nome,
            @Valid @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) SituacaoCadastro situacaoCadastro) {
        return fornecedorService.getFornecedorFilter(nome, cnpj, situacaoCadastro, PageRequest.of(page, size));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FornecedorResponseDto> updateFornecedor(@PathVariable Long id, @Valid @RequestBody FornecedorRequestDto requestDto) {
        return ResponseEntity.ok(fornecedorService.updateFornecedor(id, requestDto));
    }

    @DeleteMapping("/situacao/{id}")
    public ResponseEntity<String> deleteFornecedor(@PathVariable Long id) {
        fornecedorService.alteraSituacao(id);
        return ResponseEntity.ok().body("situação alterada");
    }

}
