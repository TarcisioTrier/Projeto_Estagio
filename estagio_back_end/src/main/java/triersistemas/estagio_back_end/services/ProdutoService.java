package triersistemas.estagio_back_end.services;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.enuns.TipoProduto;

import java.util.List;

public interface ProdutoService {
    ProdutoResponseDto getProdutoById(Long id);

    Page<ProdutoResponseDto> getProdutoPaged(String nome, TipoProduto tipo, Long grupoProdutoId, Pageable pageable);

    ProdutoResponseDto addProduto(@Valid ProdutoRequestDto produtoRequestDto);

    ProdutoResponseDto updateProduto(Long id, @Valid ProdutoRequestDto produtoRequestDto);

    ProdutoResponseDto deleteProduto(Long id);

    List<ProdutoResponseDto> getAllProdutoAlteraPreco();

    ProdutoResponseDto removeProduto(Long id);


//    List<ProdutoResponseDto> alteraMargemProduto(AtualizaPrecoDto atualizaProduto);
}