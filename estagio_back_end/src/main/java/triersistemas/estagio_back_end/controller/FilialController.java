package triersistemas.estagio_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import triersistemas.estagio_back_end.dto.FilialRequestDto;
import triersistemas.estagio_back_end.services.FilialService;

@RestController
@RequestMapping("/filiais")
public class FilialController {

    private final FilialService filialService;

    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> postFilial(@Valid @RequestBody FilialRequestDto requestDto){
            return ResponseEntity.ok(filialService.addFilial(requestDto));
    }


}
