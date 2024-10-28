package triersistemas.estagio_back_end.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.ProdutoRepository;
import triersistemas.estagio_back_end.services.GrupoProdutoService;
import triersistemas.estagio_back_end.services.ProdutoService;
import triersistemas.estagio_back_end.validators.BarcodeValidator;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final GrupoProdutoService grupoProdutoService;
    private final BarcodeValidator barcodeValidator;

    @Autowired
    public ProdutoServiceImpl(ProdutoRepository produtoRepository, GrupoProdutoService grupoProdutoService, BarcodeValidator barcodeValidator) {
        this.produtoRepository = produtoRepository;
        this.grupoProdutoService = grupoProdutoService;
        this.barcodeValidator = barcodeValidator;
    }

    @Override
    public ProdutoResponseDto getProdutoById(Long id) {
        var produto = produtoById(id);
        return new ProdutoResponseDto(produto);
    }

    @Override
    public ProdutoResponseDto addProduto(ProdutoRequestDto produtoDto) {
        var grupoProduto = grupoProdutoService.grupoProdutoById(produtoDto.grupoProdutoId());
        barcodeValidator.validateBarcodePost(produtoDto.codigoBarras(), grupoProduto.getFilial().getId());
        var produto = new Produto(produtoDto, grupoProduto);
        var saved = produtoRepository.save(produto);
        return new ProdutoResponseDto(saved);
    }

    @Override
    public ProdutoResponseDto updateProduto(Long id, ProdutoRequestDto produtoDto) {
        var grupoProduto = grupoProdutoService.buscaGrupoProdutoPorId(produtoDto.grupoProdutoId());
        var produto = produtoById(id);
        produto.atualizaProduto(produtoDto, grupoProduto);
        var saved = produtoRepository.save(produto);
        return new ProdutoResponseDto(saved);
    }

    @Override
    public ProdutoResponseDto deleteProduto(Long id) {
        var produto = produtoById(id);
        produtoRepository.delete(produto);
        return new ProdutoResponseDto(produto);
    }

    @Override
    public ProdutoResponseDto removeProduto(Long id) {
        var produto = produtoById(id);
        produto.setSituacaoCadastro(SituacaoCadastro.INATIVO);
        var saved = produtoRepository.save(produto);
        return new ProdutoResponseDto(saved);
    }

    @Override
    public Page<ProdutoResponseDto> getProdutoPaged(ProdutoPagedRequestDto pagedDto, Long filialId, Pageable pageable) {
        return produtoRepository.buscarProduto(pagedDto, filialId, pageable);
    }

    @Override
    public List<ProdutoResponseDto> getAllProdutoAlteraPreco() {
        return produtoRepository.getAllProdutoAlteraPreco();
    }

    Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    Produto produtoById(Long id) {
        return buscarProdutoPorId(id).orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

}
