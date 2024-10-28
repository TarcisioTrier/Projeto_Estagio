package triersistemas.estagio_back_end.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;

import java.util.List;


public interface GrupoProdutoRepositoryCustom {

    List<GrupoProdutoResponseDto> getAllGrupoProdutoAlteraPreco();

    List<GrupoProduto> buscarGrupoProduto(GrupoProdutoPagedRequestDto grupoProdutoDto, Long filialId);

    List<GrupoProdutoResponseDto> buscarGrupoProduto(String nomeGrupo, Long filialId);

    Page<GrupoProdutoResponseDto> buscarGrupoProduto(GrupoProdutoPagedRequestDto grupoProdutoDto, Long idFilial, Pageable pageable);
}
