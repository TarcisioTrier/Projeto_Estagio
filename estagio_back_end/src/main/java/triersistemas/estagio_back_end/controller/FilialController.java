package triersistemas.estagio_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.services.FilialService;

@RestController
@RequestMapping("/filiais")
public class FilialController {

    private final FilialService filialService;

    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @PostMapping("/post")
    public ResponseEntity<FilialResponseDto> postFilial(@Valid @RequestBody FilialRequestDto requestDto) {
        return ResponseEntity.ok(filialService.addFilial(requestDto));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<FilialResponseDto> getFilialById(@PathVariable Long id) {
        return ResponseEntity.ok(filialService.getFilialById(id));
    }

    @GetMapping("/getAllFilter")
    public Page<FilialResponseDto> getFilialFilter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nome,
            @Valid @RequestParam(required = false) String cnpj) {
        Pageable pageable = PageRequest.of(page, size);
        return filialService.getFilialFilter(nome, cnpj, pageable);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FilialResponseDto> updateFilial(@PathVariable Long id, @RequestBody FilialRequestDto requestDto) {
        return ResponseEntity.ok(filialService.updateFilial(id, requestDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFilial(@PathVariable Long id) {
        filialService.deleteFilial(id);
        return ResponseEntity.ok().body("filial deletada");
    }


}
