package triersistemas.estagio_back_end.services.impl;

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


    public ProdutoServiceImpl(ProdutoRepository produtoRepository, GrupoProdutoService grupoProdutoService, BarcodeValidator barcodeValidator) {
        this.produtoRepository = produtoRepository;
        this.grupoProdutoService = grupoProdutoService;
        this.barcodeValidator = barcodeValidator;
    }

    @Override
    public ProdutoResponseDto getProdutoById(Long id) {
        var produto = findById(id);
        return new ProdutoResponseDto(produto);
    }

    @Override
    public ProdutoResponseDto addProduto(ProdutoRequestDto produtoDto) {
        var grupoProduto = grupoProdutoService.findById(produtoDto.grupoProdutoId());
        barcodeValidator.validateBarcodePost(produtoDto.codigoBarras(), grupoProduto.getFilial().getId());
        var produto = new Produto(produtoDto, grupoProduto);
        var saved = produtoRepository.save(produto);
        return new ProdutoResponseDto(saved);
    }

    @Override
    public ProdutoResponseDto updateProduto(Long id, ProdutoRequestDto produtoDto) {
        var grupoProduto = grupoProdutoService.buscaGrupoProdutoPorId(produtoDto.grupoProdutoId());
        Long filialId = grupoProduto.map(p -> p.getFilial().getId()).orElseThrow(() -> new NotFoundException("Grupo Produto não encontrado."));
        var produto = findById(id);
        barcodeValidator.validateBarcodeUpdate(produtoDto.codigoBarras(),id,filialId);
        produto.atualizaProduto(produtoDto, grupoProduto);
        var saved = produtoRepository.save(produto);
        return new ProdutoResponseDto(saved);
    }

    @Override
    public ProdutoResponseDto deleteProduto(Long id) {
        var produto = findById(id);
        produtoRepository.delete(produto);
        return new ProdutoResponseDto(produto);
    }

    @Override
    public Page<ProdutoResponseDto> getProdutoPaged(ProdutoPagedRequestDto pagedDto, Long filialId, Pageable pageable) {
        return produtoRepository.buscarProduto(pagedDto, filialId, pageable);
    }


   private Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    private Produto findById(Long id) {
        return buscarProdutoPorId(id).orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

}
