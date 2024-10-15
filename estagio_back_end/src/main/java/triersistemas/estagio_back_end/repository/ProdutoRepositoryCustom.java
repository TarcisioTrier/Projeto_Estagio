package triersistemas.estagio_back_end.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.enuns.TipoProduto;

import java.util.List;

public interface ProdutoRepositoryCustom {
    Page<ProdutoResponseDto> buscarProduto(String nome, TipoProduto tipo, Long grupoProdutoId, Pageable pageable);

    List<ProdutoResponseDto> buscarProduto(String nome, Long grupoProdutoId);
}
