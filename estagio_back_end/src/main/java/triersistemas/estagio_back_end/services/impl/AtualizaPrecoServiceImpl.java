package triersistemas.estagio_back_end.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepository;
import triersistemas.estagio_back_end.repository.ProdutoRepository;
import triersistemas.estagio_back_end.services.AtualizaPrecoService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtualizaPrecoServiceImpl implements AtualizaPrecoService {

    private final ProdutoRepository produtoRepository;
    private final GrupoProdutoRepository grupoProdutoRepository;

    @Autowired
    public AtualizaPrecoServiceImpl(ProdutoRepository produtoRepository, GrupoProdutoRepository grupoProdutoRepository) {
        this.produtoRepository = produtoRepository;
        this.grupoProdutoRepository = grupoProdutoRepository;
    }

    @Override
    public List<ProdutoResponseDto> alteraPrecoProduto(AtualizaPrecoDto atualizaPrecoDto) {
        List<Produto> produtos = produtoRepository.findAllById(atualizaPrecoDto.produtoId());
        produtos.forEach(produto -> {
            produto.setMargemLucro(atualizaPrecoDto.margemLucroAtualizada());
            produto.setDataUltimaAtualizacaoPreco(LocalDate.now());
            produto.calculateValorVenda();
        });
        produtoRepository.saveAll(produtos);
        return produtos.stream().map(ProdutoResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<GrupoProdutoResponseDto> alteraPrecoGrupoProduto(AtualizaPrecoDto atualizaProduto) {
        List<GrupoProduto> grupoProdutos = grupoProdutoRepository.findAllById(atualizaProduto.grupoProdutoId());
        List<Produto> produtosParaAtualizar = new ArrayList<>();

        grupoProdutos.forEach(grupoProduto -> {
            grupoProduto.setMargemLucro(atualizaProduto.margemLucroAtualizada());

            grupoProduto.getProdutos().forEach(produto -> {
                if (produto.getMargemLucro() == null) {
                    produto.setDataUltimaAtualizacaoPreco(LocalDate.now());
                    produto.calculateValorVenda();
                    produtosParaAtualizar.add(produto);
                }
            });
        });

        grupoProdutoRepository.saveAll(grupoProdutos);
        produtoRepository.saveAll(produtosParaAtualizar);

        return grupoProdutos.stream().map(GrupoProdutoResponseDto::new).collect(Collectors.toList());
    }
}