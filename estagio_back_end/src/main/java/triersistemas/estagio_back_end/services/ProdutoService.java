package triersistemas.estagio_back_end.services;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.enuns.TipoProduto;

public interface ProdutoService {
    ProdutoResponseDto getProdutoById(Long id);

    Page<ProdutoResponseDto> getProdutoFilter(String nome, TipoProduto tipo, Long grupoProdutoId, Pageable pageable);

    ProdutoResponseDto addProduto(@Valid ProdutoRequestDto produtoRequestDto);

    ProdutoResponseDto updateProduto(Long id, @Valid ProdutoRequestDto produtoRequestDto);

    ProdutoResponseDto deleteProdutoById(Long id);

    ProdutoResponseDto alteraProdutoById(Long id, boolean ativar);
}
