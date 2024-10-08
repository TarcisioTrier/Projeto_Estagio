package triersistemas.estagio_back_end.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.ProdutoRepository;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.services.GrupoProdutoService;
import triersistemas.estagio_back_end.services.ProdutoService;

import java.util.Optional;

@Service
public class ProdutoServiceImpl implements ProdutoService {
    @Autowired
    ProdutoRepository produtoRepository;
    @Autowired
    GrupoProdutoService grupoProdutoService;


    @Override
    public ProdutoResponseDto getProdutoById(Long id) {
        var produto = produtoById(id);
        return new ProdutoResponseDto(produto);
    }

    @Override
    public Page<ProdutoResponseDto> getProdutoFilter(String nome, TipoProduto tipo, Long grupoProdutoId, Pageable pageable) {
        return produtoRepository.buscarProduto(nome, tipo, grupoProdutoId, pageable);
    }

    @Override
    public ProdutoResponseDto addProduto(ProdutoRequestDto produtoRequestDto) {
        var grupoProduto = grupoProdutoService.grupoProdutoById(produtoRequestDto.grupoProdutoId());
        var produto = new Produto(produtoRequestDto, grupoProduto);
        var saved = produtoRepository.save(produto);
        return new ProdutoResponseDto(saved);
    }

    @Override
    public ProdutoResponseDto updateProduto(Long id, ProdutoRequestDto produtoRequestDto) {
        var grupoProduto = grupoProdutoService.buscaGrupoProdutoPorId(produtoRequestDto.grupoProdutoId());
        var produto = produtoById(id);
        produto.atualizaProduto(produtoRequestDto, grupoProduto);
        var saved = produtoRepository.save(produto);
        return new ProdutoResponseDto(saved);
    }

    @Override
    public ProdutoResponseDto deleteProdutoById(Long id) {
        var produto = produtoById(id);
        produtoRepository.delete(produto);
        return new ProdutoResponseDto(produto);
    }

    @Override
    public ProdutoResponseDto alteraProdutoById(Long id, boolean ativar) {
        var produto = produtoById(id);
        if (ativar) {
            produto.setSituacaoCadastro(SituacaoCadastro.ATIVO);
        }else{
            produto.setSituacaoCadastro(SituacaoCadastro.INATIVO);
        }
        var saved = produtoRepository.save(produto);
        return new ProdutoResponseDto(saved);
    }

    Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    Produto produtoById(Long id) {
        return buscarProdutoPorId(id).orElseThrow(()-> new NotFoundException("Produto não encontrado"));
    }

}
