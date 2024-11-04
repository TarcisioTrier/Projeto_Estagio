package triersistemas.estagio_back_end.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.FilialPagedRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Filial;

import java.util.List;

public interface FilialRepositoryCustom {
    Page<FilialResponseDto> buscarFiliais(String nome, String cnpj, Pageable pageable);

    List<FilialResponseDto> buscarFiliais(String nome);

    Page<FilialResponseDto> buscarFiliais(FilialPagedRequestDto filialDto, Pageable pageable);
}
