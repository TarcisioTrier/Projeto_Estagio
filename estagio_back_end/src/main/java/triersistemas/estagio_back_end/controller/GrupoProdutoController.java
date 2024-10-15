package triersistemas.estagio_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;
import triersistemas.estagio_back_end.services.GrupoProdutoService;

import java.util.List;

@RestController
@RequestMapping("/grupos-produtos")
public class GrupoProdutoController {
    final GrupoProdutoService grupoProdutoService;

    public GrupoProdutoController(GrupoProdutoService grupoProdutoService) {
        this.grupoProdutoService = grupoProdutoService;
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<GrupoProdutoResponseDto> getGrupoProdutoById(@PathVariable Long id) {
        return ResponseEntity.ok(grupoProdutoService.getGrupoProdutoById(id));
    }

    @GetMapping("/getAllPaged")
    public Page<GrupoProdutoResponseDto> getGrupoProdutoPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long idFilial,
            @RequestParam(required = false) String nomeGrupo,
            @RequestParam(required = false) TipoGrupoProduto tipoGrupo) {
        Pageable pageable = PageRequest.of(page, size);
        return grupoProdutoService.getGrupoProdutoFilter(nomeGrupo, tipoGrupo, idFilial, pageable);
    }

    @GetMapping("/getAllFilter")
    public List<GrupoProdutoResponseDto> getGrupoProdutoFilter(
            @RequestParam(required = false) Long filialId,
            @RequestParam(required = false) String nomeGrupo){
        return grupoProdutoService.getGrupoProdutoFilter(nomeGrupo, filialId);
    }


    @PostMapping("/post")
    public ResponseEntity<GrupoProdutoResponseDto> postGrupoProduto(@Valid @RequestBody GrupoProdutoRequestDto grupoProdutoRequestDto) {
        return ResponseEntity.ok(grupoProdutoService.addGrupoProduto(grupoProdutoRequestDto));
    }
    @PutMapping("/put/{id}")
    public ResponseEntity<GrupoProdutoResponseDto> putGrupoProduto(@PathVariable Long id,@Valid @RequestBody GrupoProdutoRequestDto grupoProdutoRequestDto) {
        return ResponseEntity.ok(grupoProdutoService.updateGrupoProduto(id, grupoProdutoRequestDto));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GrupoProdutoResponseDto> deleteGrupoProdutoById(@PathVariable Long id) {
        return ResponseEntity.ok(grupoProdutoService.deleteGrupoProdutoById(id));
    }
    @DeleteMapping("/altera/{id}")
    public ResponseEntity<GrupoProdutoResponseDto> alteraGrupoProdutoById(@PathVariable Long id, @RequestParam boolean ativar) {
        return ResponseEntity.ok(grupoProdutoService.alteraGrupoProdutoById(id, ativar));
    }
}
