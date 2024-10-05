package triersistemas.estagio_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.services.FilialService;

@RestController
@RequestMapping("/filiais")
public class FilialController {

    private final FilialService filialService;

    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @PostMapping("/post")
    public ResponseEntity<?> postFilial(@Valid @RequestBody FilialRequestDto requestDto){
            return ResponseEntity.ok(filialService.addFilial(requestDto));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getFilialById(@PathVariable Long id){
        return ResponseEntity.ok(filialService.getFilialById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFilial(@PathVariable Long id){
        filialService.deleteFilial(id);
        return ResponseEntity.ok().body("filial deletada");
    }


}
