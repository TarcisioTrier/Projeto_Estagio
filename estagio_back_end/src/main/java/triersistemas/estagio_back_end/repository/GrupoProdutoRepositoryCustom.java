package triersistemas.estagio_back_end.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;


public interface GrupoProdutoRepositoryCustom {
   Page<GrupoProdutoResponseDto> buscarGrupoProduto(String nomeGrupo, TipoGrupoProduto tipoGrupo, Long idFilial, Pageable pageable);
}
