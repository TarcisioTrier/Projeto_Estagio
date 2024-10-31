package triersistemas.estagio_back_end.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Produto;

import java.util.List;

public interface ProdutoRepositoryCustom {

    List<Produto> buscarProduto(ProdutoPagedRequestDto produtoPagedDto, Long filialId);

    Page<ProdutoResponseDto> buscarProduto(ProdutoPagedRequestDto produtoPagedDto, Long filialId, Pageable pageable);
}
