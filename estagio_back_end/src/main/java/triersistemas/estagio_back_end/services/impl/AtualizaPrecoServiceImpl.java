package triersistemas.estagio_back_end.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.AtualizaPrecoEnum;
import triersistemas.estagio_back_end.exceptions.InvalidMargemException;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepository;
import triersistemas.estagio_back_end.repository.ProdutoRepository;
import triersistemas.estagio_back_end.services.AtualizaPrecoService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AtualizaPrecoServiceImpl implements AtualizaPrecoService {

    private final ProdutoRepository produtoRepository;
    private final GrupoProdutoRepository grupoProdutoRepository;
    private final FilialRepository filialRepository;

    @Autowired
    public AtualizaPrecoServiceImpl(ProdutoRepository produtoRepository, GrupoProdutoRepository grupoProdutoRepository, FilialRepository filialRepository) {
        this.produtoRepository = produtoRepository;
        this.grupoProdutoRepository = grupoProdutoRepository;
        this.filialRepository = filialRepository;
    }

    @Override
    public List<?> atualizaPreco(AtualizaPrecoDto dto) {
        var filial = filialRepository.findById(dto.filialId()).orElseThrow(() -> new NotFoundException("Filial não encontrada"));
        var grupoProdutos = filial.getGrupoProdutos();
        var produtos = getProdutos(grupoProdutos);
        if (!dto.all()) {
            if (dto.isProduto()) {
                produtos = produtos.stream().filter(produto -> dto.produtoId().contains(produto.getId())).toList();
            } else {
                grupoProdutos = grupoProdutos.stream().filter(grupoProduto -> dto.grupoProdutoId().contains(grupoProduto.getId())).toList();
                produtos = getProdutos(grupoProdutos);
            }
        }

        var saved = produtoRepository.saveAll(atualiza(produtos, grupoProdutos, dto));

        return saved.stream().map(ProdutoResponseDto::new).toList();
    }

    private List<Produto> atualiza(List<Produto> produtos, List<GrupoProduto> grupoProdutos, AtualizaPrecoDto dto) {
        if (dto.atualizaPreco() == AtualizaPrecoEnum.MARGEM) {
            if (!dto.isProduto()) {
                return atualizaGrupoMargem(grupoProdutos, dto);
            }
            return atualizaMargem(produtos, dto);
        }
        return atualizaPrecoProduto(produtos, dto);

    }


    private List<Produto> atualizaPrecoProduto(List<Produto> produtos, AtualizaPrecoDto dto) {
        produtos = atualizavel(produtos);

        if (dto.atualizaPreco() == AtualizaPrecoEnum.VALOR_PRODUTO || dto.atualizaPreco() == AtualizaPrecoEnum.VALOR_VENDA) {
            produtos = atualizaValor(produtos, dto);
        } else {
            throw new NotFoundException("Tipo de atualização não encontrado");
        }
        return produtos;
    }


    private List<Produto> atualizaValor(List<Produto> produtos, AtualizaPrecoDto dto) {
        return produtos.stream().map(produto -> {
            if (dto.atualizaPreco() == AtualizaPrecoEnum.VALOR_PRODUTO) {
                var valor = valorSelecter1(dto.isPercentual(), dto.isRelativo(), produto.getValorProduto(), dto.valor());
                testeNegativo(valor);
                produto.setValorProduto(valor);
                produto.calculateValorVenda();

            } else {
                var valor = valorSelecter1(dto.isPercentual(), dto.isRelativo(), produto.getValorVenda(), dto.valor());
                testeNegativo(valor);
                produto.setValorVenda(valor);
                produto.calculateMargemLucro();
            }
            return produto;
        }).toList();

    }

    private List<Produto> atualizaMargem(List<Produto> produtos, AtualizaPrecoDto dto) {
        return produtos.stream().map(produto -> {
            var valor = valorSelecter(dto.isRelativo(), produto.getMargemLucro().add(dto.valor()), dto.valor());
            testeNegativo(valor);
            testePositivo(valor);
            produto.setMargemLucro(valor);
            produto.calculateValorVenda();
            return produto;
        }).toList();
    }


    private List<Produto> atualizaGrupoMargem(List<GrupoProduto> grupoProdutos, AtualizaPrecoDto dto) {
        grupoProdutos = grupoProdutos.stream().map(grupoProduto -> {
            var valor = valorSelecter(dto.isRelativo(), grupoProduto.getMargemLucro().add(dto.valor()), dto.valor());
            testeNegativo(valor);
            testePositivo(valor);
            grupoProduto.setMargemLucro(valor);
            return grupoProduto;
        }).toList();
        var produtos = grupoProdutos.stream().map(GrupoProduto::getProdutos).flatMap(List::stream).toList();
        produtos.forEach(Produto::calculateValorVenda);
        grupoProdutoRepository.saveAll(grupoProdutos);
        return produtos;

    }

    private void testeNegativo(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) < 0)
            throw new InvalidMargemException("Valor não pode ser negativo");
    }

    private void testePositivo(BigDecimal margem) {
        if (margem.compareTo(BigDecimal.valueOf(99.99)) >= 0)
            throw new InvalidMargemException("Valor da Margem não pode ser maior que ou igual a 100%");
    }

    private BigDecimal percent(BigDecimal valor) {
        return valor.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
    }
    private List<Produto> atualizavel(List<Produto> produtos) {
        return produtos.stream().filter(produto -> produto.getAtualizaPreco() && produto.getGrupoProduto().getAtualizaPreco()).toList();
    }
    private List<Produto> getProdutos(List<GrupoProduto> grupoProdutos) {
        return grupoProdutos.stream().map(GrupoProduto::getProdutos).flatMap(List::stream).toList();
    }

    private BigDecimal valorSelecter1(Boolean isPercentual, Boolean isRelativo, BigDecimal a, BigDecimal valor) {
        if (isPercentual) {
            if(isRelativo)
                return a.multiply(BigDecimal.ONE.add(percent(valor)));
            return a.multiply(percent(valor));
        }
        if(isRelativo)
            return a.add(valor);
        return valor;
    }

    private BigDecimal valorSelecter(Boolean isRelativo, BigDecimal relativo, BigDecimal absoluto) {
        if (isRelativo)
            return relativo;
        return absoluto;
    }
}


