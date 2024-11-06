package triersistemas.estagio_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triersistemas.estagio_back_end.dto.request.FornecedorPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.services.FornecedorService;

import java.util.List;

@RestController
@RequestMapping("fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping("/post")
    public ResponseEntity<FornecedorResponseDto> postFornecedor(@Valid @RequestBody FornecedorRequestDto fornecedorDto) {
        return ResponseEntity.ok(fornecedorService.addFornecedor(fornecedorDto));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<FornecedorResponseDto> getFornecedorById(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.getFornecedorById(id));
    }

    @PutMapping("/getAllPaged")
    public Page<FornecedorResponseDto> getFornecedorPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Valid @RequestParam(required = false) Long filialId,
            @RequestBody(required = false) FornecedorPagedRequestDto fornecedorDto) {
        Pageable pageable = PageRequest.of(page, size);
        return fornecedorService.getFornecedorPaged(filialId, fornecedorDto, pageable);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FornecedorResponseDto> updateFornecedor(@PathVariable Long id, @Valid @RequestBody FornecedorRequestDto fornecedorDto) {
        return ResponseEntity.ok(fornecedorService.updateFornecedor(id, fornecedorDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FornecedorResponseDto> deleteFornecedor(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.deleteFornecedor(id));
    }

}
