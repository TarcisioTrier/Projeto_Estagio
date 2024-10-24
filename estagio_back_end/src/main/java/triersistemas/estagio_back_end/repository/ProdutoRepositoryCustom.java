package triersistemas.estagio_back_end.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.PagedRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;

import java.util.List;

public interface ProdutoRepositoryCustom {
    List<ProdutoResponseDto> getAllProdutoAlteraPreco();

    Page<ProdutoResponseDto> buscarProduto(PagedRequestDto produtoPagedDto, Long filialId, Pageable pageable);
}
