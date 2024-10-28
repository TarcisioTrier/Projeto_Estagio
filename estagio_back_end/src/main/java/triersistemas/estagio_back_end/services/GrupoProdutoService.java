package triersistemas.estagio_back_end.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;

import java.util.List;
import java.util.Optional;

public interface GrupoProdutoService {
    GrupoProdutoResponseDto getGrupoProdutoById(Long id);

    GrupoProdutoResponseDto addGrupoProduto(GrupoProdutoRequestDto grupoProdutoRequestDto);

    GrupoProdutoResponseDto updateGrupoProduto(Long id, GrupoProdutoRequestDto grupoProdutoRequestDto);

    GrupoProdutoResponseDto deleteGrupoProduto(Long id);

    GrupoProdutoResponseDto removeGrupoProduto(Long id);

    Optional<GrupoProduto> buscaGrupoProdutoPorId(Long id);

    GrupoProduto grupoProdutoById(Long id);

    List<GrupoProdutoResponseDto> getGrupoProdutoFilter(String nomeGrupo, Long filialId);

    Page<GrupoProdutoResponseDto> getGrupoProdutoPaged(GrupoProdutoPagedRequestDto grupoProdutoDto, Long filialId, Pageable pageable);
}
