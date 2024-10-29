package triersistemas.estagio_back_end.services.impl;

import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.AtualizaPrecoEnum;
import triersistemas.estagio_back_end.exceptions.InvalidMargemException;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepository;
import triersistemas.estagio_back_end.repository.ProdutoRepository;
import triersistemas.estagio_back_end.services.AtualizaPrecoService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AtualizaPrecoServiceImpl implements AtualizaPrecoService {

    private final ProdutoRepository produtoRepository;
    private final GrupoProdutoRepository grupoProdutoRepository;


    public AtualizaPrecoServiceImpl(ProdutoRepository produtoRepository, GrupoProdutoRepository grupoProdutoRepository) {
        this.produtoRepository = produtoRepository;
        this.grupoProdutoRepository = grupoProdutoRepository;
    }

    @Override
    public List<?> atualizaPreco(AtualizaPrecoDto atualizaPrecoDto) {
        var grupoProdutos = Optional.ofNullable(grupoProdutoRepository.buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), atualizaPrecoDto.filialId())).orElse(List.of());
        var produtos = Optional.ofNullable(produtoRepository.buscarProduto(atualizaPrecoDto.produtoFilter(), atualizaPrecoDto.filialId())).orElse(List.of());

        if (!atualizaPrecoDto.all()) {
            if (atualizaPrecoDto.isProduto()) {
                produtos = produtos.stream().filter(produto -> atualizaPrecoDto.produtoId().contains(produto.getId())).toList();
            } else {
                grupoProdutos = grupoProdutos.stream().filter(grupoProduto -> atualizaPrecoDto.grupoProdutoId().contains(grupoProduto.getId())).toList();
                produtos = getProdutos(grupoProdutos);
            }
        }
        var produtosAtt = atualiza(produtos, grupoProdutos, atualizaPrecoDto);
        produtosAtt.forEach(produto -> produto.setDataUltimaAtualizacaoPreco(LocalDate.now()));
        var saved = produtoRepository.saveAll(produtosAtt);

        return saved.stream().map(ProdutoResponseDto::new).toList();
    }

    private List<Produto> atualiza(List<Produto> produtos, List<GrupoProduto> grupoProdutos, AtualizaPrecoDto atualizaPrecoDto) {
        if (atualizaPrecoDto.atualizaPreco() == AtualizaPrecoEnum.MARGEM) {
            if (!atualizaPrecoDto.isProduto()) {
                return atualizaGrupoMargem(grupoProdutos, atualizaPrecoDto);
            }
            return atualizaMargem(produtos, atualizaPrecoDto);
        }
        return atualizaPrecoProduto(produtos, atualizaPrecoDto);

    }


    private List<Produto> atualizaPrecoProduto(List<Produto> produtos, AtualizaPrecoDto atualizaPrecoDto) {
        produtos = atualizavel(produtos);

        if (atualizaPrecoDto.atualizaPreco() == AtualizaPrecoEnum.VALOR_PRODUTO || atualizaPrecoDto.atualizaPreco() == AtualizaPrecoEnum.VALOR_VENDA)
            return atualizaValor(produtos, atualizaPrecoDto);
        throw new NotFoundException("Tipo de atualização não encontrado");

    }


    private List<Produto> atualizaValor(List<Produto> produtos, AtualizaPrecoDto atualizaPrecoDto) {
        return produtos.stream().map(produto -> {
            if (atualizaPrecoDto.atualizaPreco() == AtualizaPrecoEnum.VALOR_PRODUTO) {
                var valor = valorCalculo(atualizaPrecoDto.isPercentual(), atualizaPrecoDto.isRelativo(), produto.getValorProduto(), atualizaPrecoDto.valor());
                testeValor(valor);
                produto.setValorProduto(valor);
                produto.calculateValorVenda();

            } else {
                var valor = valorCalculo(atualizaPrecoDto.isPercentual(), atualizaPrecoDto.isRelativo(), produto.getValorVenda(), atualizaPrecoDto.valor());
                testeValor(valor);
                produto.setValorVenda(valor);
                produto.calculateMargemLucro();
            }
            return produto;
        }).toList();

    }

    private List<Produto> atualizaMargem(List<Produto> produtos, AtualizaPrecoDto atualizaPrecoDto) {
        return produtos.stream().map(produto -> {
            var valor = valorCalculoRelativo(atualizaPrecoDto.isRelativo(), produto.getMargemLucro().add(atualizaPrecoDto.valor()), atualizaPrecoDto.valor());
            testeMargem(valor);
            produto.setMargemLucro(valor);
            produto.calculateValorVenda();
            return produto;
        }).toList();
    }


    private List<Produto> atualizaGrupoMargem(List<GrupoProduto> grupoProdutos, AtualizaPrecoDto atualizaPrecoDto) {
        grupoProdutos = grupoProdutos.stream().map(grupoProduto -> {
            var valor = valorCalculoRelativo(atualizaPrecoDto.isRelativo(), grupoProduto.getMargemLucro().add(atualizaPrecoDto.valor()), atualizaPrecoDto.valor());
            testeMargem(valor);
            grupoProduto.setMargemLucro(valor);
            return grupoProduto;
        }).toList();
        var produtos = grupoProdutos.stream().map(GrupoProduto::getProdutos).flatMap(List::stream).toList();
        produtos.forEach(Produto::calculateValorVenda);
        grupoProdutoRepository.saveAll(grupoProdutos);
        return produtos;

    }

    private void testeValor(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidMargemException("Valor não pode ser negativo");
    }

    private void testeMargem(BigDecimal margem) {
        if (margem.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidMargemException("Valor da Margem não pode ser negativo");
        if (margem.compareTo(BigDecimal.valueOf(99.99)) >= 0)
            throw new InvalidMargemException("Valor da Margem não pode ser maior que ou igual a 100%");
    }

    private static BigDecimal percent(BigDecimal valor) {
        return valor.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
    }

    private List<Produto> atualizavel(List<Produto> produtos) {
        return produtos.stream().filter(produto -> produto.getAtualizaPreco() && produto.getGrupoProduto().getAtualizaPreco()).toList();
    }

    private List<Produto> getProdutos(List<GrupoProduto> grupoProdutos) {
        return grupoProdutos.stream().map(GrupoProduto::getProdutos).flatMap(List::stream).toList();
    }

    private static BigDecimal valorCalculo(Boolean isPercentual, Boolean isRelativo, BigDecimal valorOriginal, BigDecimal valor) {
        if (isPercentual) {
            return valorCalculoRelativo(isRelativo, valorOriginal.multiply(percent(valor)), valorOriginal.multiply(percent(valor)));
        }
        return valorCalculoRelativo(isRelativo, valorOriginal.add(valor), valor);
    }

    private static BigDecimal valorCalculoRelativo(Boolean isRelativo, BigDecimal relativo, BigDecimal absoluto) {
        if (isRelativo)
            return relativo;
        return absoluto;
    }
}


