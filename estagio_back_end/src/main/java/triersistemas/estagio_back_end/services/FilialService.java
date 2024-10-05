package triersistemas.estagio_back_end.services;

import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;

public interface FilialService {

    FilialResponseDto addFilial(FilialRequestDto requestDto);

    FilialResponseDto updateFilial(Long id, FilialRequestDto requestDto);

    void deleteFilial(Long id);

    Filial getFilialById(Long id);
}
