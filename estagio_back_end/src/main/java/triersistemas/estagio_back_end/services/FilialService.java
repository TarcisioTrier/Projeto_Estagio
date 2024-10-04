package triersistemas.estagio_back_end.services;

import triersistemas.estagio_back_end.dto.FilialRequestDto;
import triersistemas.estagio_back_end.dto.FilialResponseDto;

public interface FilialService {

    FilialResponseDto addFilial(FilialRequestDto requestDto);

    FilialResponseDto updateFilial(Long id, FilialRequestDto requestDto);

    void deleteFilial(Long id);

    FilialResponseDto getFilialById(Long id);
}
