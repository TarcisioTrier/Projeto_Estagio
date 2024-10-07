package triersistemas.estagio_back_end.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;

public interface FilialRepositoryCustom {
    Page<FilialResponseDto> buscarFiliais(String nome, String cnpj, Pageable pageable);
}
