package triersistemas.estagio_back_end.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.FilialPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialChartDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;

import java.util.List;
import java.util.Optional;

public interface FilialService {

    FilialResponseDto addFilial(FilialRequestDto requestDto);

    FilialResponseDto updateFilial(Long id, FilialRequestDto requestDto);

    FilialResponseDto deleteFilial(Long id);

    FilialResponseDto getFilialById(Long id);

    List<FilialResponseDto> getAllFilial();

    Page<FilialResponseDto> getFilialFilter(String nome, String cnpj, Pageable pageable);

    Optional<Filial> buscaFilialPorId(Long id);

    Filial findById(Long id);

    List<FilialResponseDto> getFilialFilter(String nome);

    FilialResponseDto inativaFilial(Long id);

    Page<FilialResponseDto> getFilialFPaged(FilialPagedRequestDto filialDto, Pageable pageable);

    List<FilialChartDto> getChart();
}
