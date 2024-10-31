package triersistemas.estagio_back_end.services;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Produto;

import java.util.List;

public interface ProdutoService {
    ProdutoResponseDto getProdutoById(Long id);

    ProdutoResponseDto addProduto(@Valid ProdutoRequestDto produtoRequestDto);

    ProdutoResponseDto updateProduto(Long id, @Valid ProdutoRequestDto produtoRequestDto);

    ProdutoResponseDto deleteProduto(Long id);

    Page<ProdutoResponseDto> getProdutoPaged(ProdutoPagedRequestDto produtoPagedDto, Long filialId, Pageable pageable);

}