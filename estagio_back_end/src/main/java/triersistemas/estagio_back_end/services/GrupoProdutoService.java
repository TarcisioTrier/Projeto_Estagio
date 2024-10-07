package triersistemas.estagio_back_end.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;

public interface GrupoProdutoService {
    GrupoProdutoResponseDto getGrupoProdutoById(Long id);

    GrupoProdutoResponseDto addGrupoProduto(GrupoProdutoRequestDto grupoProdutoRequestDto);

    GrupoProdutoResponseDto updateGrupoProduto(Long id, GrupoProdutoRequestDto grupoProdutoRequestDto);

    GrupoProdutoResponseDto deleteGrupoProdutoById(Long id);

    Page<GrupoProdutoResponseDto> getGrupoProdutoFilter(String nomeGrupo, TipoGrupoProduto tipoGrupo, Long idFilial, Pageable pageable);
}
