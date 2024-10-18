package triersistemas.estagio_back_end.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.ProdutoRepository;
import triersistemas.estagio_back_end.services.AtualizaPrecoService;
import triersistemas.estagio_back_end.services.GrupoProdutoService;
import triersistemas.estagio_back_end.services.ProdutoService;
import triersistemas.estagio_back_end.validators.BarcodeValidator;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final GrupoProdutoService grupoProdutoService;
    private final AtualizaPrecoService atualizaPrecoService;
    private final BarcodeValidator barcodeValidator;

    @Autowired
    public ProdutoServiceImpl(ProdutoRepository produtoRepository,
                              GrupoProdutoService grupoProdutoService,
                              AtualizaPrecoService atualizaPrecoService, BarcodeValidator barcodeValidator) {
        this.produtoRepository = produtoRepository;
        this.grupoProdutoService = grupoProdutoService;
        this.atualizaPrecoService = atualizaPrecoService;
        this.barcodeValidator = barcodeValidator;
    }

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
        barcodeValidator.validateBarcodePost(produtoRequestDto.codigoBarras(), grupoProduto.getFilial().getId());
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
        } else {
            produto.setSituacaoCadastro(SituacaoCadastro.INATIVO);
        }
        var saved = produtoRepository.save(produto);
        return new ProdutoResponseDto(saved);
    }

    @Override
    public List<ProdutoResponseDto> getAllProdutoAlteraPreco() {
        return produtoRepository.getAllProdutoAlteraPreco();
    }

    @Override
    public List<ProdutoResponseDto> alteraMargemProduto(AtualizaPrecoDto atualizaProduto) {
        return atualizaPrecoService.alteraMargemProduto(atualizaProduto);
    }

    Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    Produto produtoById(Long id) {
        return buscarProdutoPorId(id).orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

}
