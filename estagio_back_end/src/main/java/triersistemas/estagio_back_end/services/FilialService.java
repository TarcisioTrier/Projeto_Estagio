package triersistemas.estagio_back_end.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;

import java.util.List;
import java.util.Optional;

public interface FilialService {

    FilialResponseDto addFilial(FilialRequestDto requestDto);

    FilialResponseDto updateFilial(Long id, FilialRequestDto requestDto);

    void deleteFilial(Long id);

    FilialResponseDto getFilialById(Long id);

    List<FilialResponseDto> getAllFiliais();

    Page<FilialResponseDto> getFilialFilter(String nome, String cnpj, Pageable pageable);

    Optional<Filial> buscaFilialPorId(Long id);
}
