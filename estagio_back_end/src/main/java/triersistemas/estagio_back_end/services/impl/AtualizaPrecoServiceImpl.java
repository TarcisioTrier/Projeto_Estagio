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
import triersistemas.estagio_back_end.repository.GrupoProdutoRepository;
import triersistemas.estagio_back_end.repository.ProdutoRepository;
import triersistemas.estagio_back_end.services.AtualizaPrecoService;
import triersistemas.estagio_back_end.services.FilialService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Service
public class AtualizaPrecoServiceImpl implements AtualizaPrecoService {

    private final ProdutoRepository produtoRepository;
    private final GrupoProdutoRepository grupoProdutoRepository;
    private final FilialService filialService;

    @Autowired
    public AtualizaPrecoServiceImpl(ProdutoRepository produtoRepository, GrupoProdutoRepository grupoProdutoRepository, FilialService filialService) {
        this.produtoRepository = produtoRepository;
        this.grupoProdutoRepository = grupoProdutoRepository;
        this.filialService = filialService;
    }

    @Override
    public List<?> atualizaPreco(AtualizaPrecoDto dto) {
        if (dto.isProduto()) {
            return atualizaPrecoProduto(dto.all(), dto.atualizaPreco(), dto.filialId(), dto.produtoId(), dto.valor(), dto.isRelativo());
        } else {
            return atualizaPrecoGrupoProduto(dto);
        }
    }

    private List<ProdutoResponseDto> atualizaPrecoGrupoProduto(AtualizaPrecoDto dto) {
        List<GrupoProduto> grupoProdutos;
        List<Produto> produtos;
        grupoProdutos = filialService.findById(dto.filialId()).getGrupoProdutos();
        if (!dto.all()) {
            grupoProdutos = grupoProdutos.stream().filter(a-> dto.grupoProdutoId().contains(a)).toList();
        }

        switch (dto.atualizaPreco()) {
            case VALOR_PRODUTO_ABSOLUTO:
            case VALOR_PRODUTO_PERCENTUAL:
            case VALOR_VENDA_ABSOLUTO:
            case VALOR_VENDA_PERCENTUAL:
                produtos = grupoProdutos.stream().map(a-> a.getProdutos()).flatMap(List::stream).collect(Collectors.toList());;
                return atualizaPrecoProduto(dto.all(), dto.atualizaPreco(), dto.filialId(), produtos.stream().map(Produto::getId).collect(Collectors.toList()), dto.valor(), dto.isRelativo());
            case MARGEM:
                grupoProdutos = grupoProdutos.stream().filter(grupoProduto -> grupoProduto.getAtualizaPreco()).collect(Collectors.toList());
                return atualizaGrupoMargem(grupoProdutos, dto.valor(), dto.isRelativo()).stream().map(ProdutoResponseDto::new).collect(Collectors.toList());
            case null, default:
                throw new NotFoundException("Tipo de atualização não encontrado");
        }

    }

    private List<ProdutoResponseDto> atualizaPrecoProduto(Boolean all, AtualizaPrecoEnum atualizaPreco, Long filialId, List<Long> produtoId, BigDecimal valor, Boolean isRelativo) {

            List<Produto> produtos;
            produtos = filialService.findById(filialId).getProdutos();
            if (!all) {
                produtos = produtos.stream().filter(produto-> produtoId.contains(produto.getId())).toList();
            }
            produtos = produtos.stream().filter(produto -> produto.getAtualizaPreco() && produto.getGrupoProduto().getAtualizaPreco()).collect(Collectors.toList());

            switch (atualizaPreco) {
                case VALOR_PRODUTO_ABSOLUTO:
                    produtos = atualizaValorProdutoAbsoluto(produtos, valor, isRelativo);
                    break;
                case VALOR_PRODUTO_PERCENTUAL:
                    produtos = atualizaValorProdutoPercentual(produtos, valor, isRelativo);
                    break;
                case VALOR_VENDA_ABSOLUTO:
                    produtos = atualizaValorVendaAbsoluto(produtos, valor, isRelativo);
                    break;
                case VALOR_VENDA_PERCENTUAL:
                    produtos = atualizaValorVendaPercentual(produtos, valor, isRelativo);
                    break;
                case MARGEM:
                    produtos = atualizaMargem(produtos, valor, isRelativo);
                    break;
                case null, default:
                    throw new NotFoundException("Tipo de atualização não encontrado");
            }
            var saved =  produtoRepository.saveAll(produtos);
            return saved.stream().map(ProdutoResponseDto::new).toList();

    }


        private List<Produto> atualizaValorProdutoAbsoluto (List < Produto > produtos, BigDecimal valor,
        boolean isRelativo){

            return produtos.stream().map(produto -> {
                if (isRelativo) {
                    testeNegativo(produto.getValorProduto().add(valor));
                    produto.setValorProduto(produto.getValorProduto().add(valor));
                } else {
                    testeNegativo(valor);
                    produto.setValorProduto(valor);
                }
                produto.calculateValorVenda();
                return produto;

            }).collect(Collectors.toList());

        }

        private List<Produto> atualizaValorProdutoPercentual (List < Produto > produtos, BigDecimal valor,
        boolean isRelativo){
            return produtos.stream().map(produto -> {
                if (isRelativo) {
                    testeNegativo(produto.getValorProduto().multiply(BigDecimal.ONE.add(percent(valor))));
                    produto.setValorProduto(produto.getValorProduto().multiply(BigDecimal.ONE.add(percent(valor))));
                } else {
                    testeNegativo(produto.getValorProduto().multiply(percent(valor)));
                    produto.setValorProduto(produto.getValorProduto().multiply(percent(valor)));
                }
                produto.calculateValorVenda();
                return produto;
            }).collect(Collectors.toList());
        }

        private List<Produto> atualizaValorVendaAbsoluto (List < Produto > produtos, BigDecimal valor,boolean isRelativo)
        {
            return produtos.stream().map(produto -> {
                if (isRelativo) {
                    testeNegativo(produto.getValorVenda().add(valor));
                    produto.setValorVenda(produto.getValorVenda().add(valor));
                } else {
                    testeNegativo(valor);
                    produto.setValorVenda(valor);
                }
                produto.calculateValorProduto();
                return produto;
            }).collect(Collectors.toList());
        }

        private List<Produto> atualizaValorVendaPercentual (List < Produto > produtos, BigDecimal valor,
        boolean isRelativo){
            return produtos.stream().map(produto -> {
                if (isRelativo) {
                    testeNegativo(produto.getValorVenda().multiply(BigDecimal.ONE.add(percent(valor))));
                    produto.setValorVenda(produto.getValorVenda().multiply(BigDecimal.ONE.add(percent(valor))));
                } else {
                    testeNegativo(produto.getValorVenda().multiply(percent(valor)));
                    produto.setValorVenda(produto.getValorVenda().multiply(percent(valor)));
                }
                produto.calculateValorProduto();
                return produto;
            }).collect(Collectors.toList());
        }

        private List<Produto> atualizaMargem (List < Produto > produtos, BigDecimal valor,boolean isRelativo){
            return produtos.stream().map(produto -> {
                if (isRelativo) {
                    testeNegativo(produto.getMargemLucro().add(valor));
                    testePositivo(produto.getMargemLucro().add(valor));
                    produto.setMargemLucro(produto.getMargemLucro().add(valor));
                } else {
                    testeNegativo(valor);
                    testePositivo(valor);
                    produto.setMargemLucro(valor);
                }
                produto.calculateValorVenda();
                return produto;
            }).collect(Collectors.toList());
        }

        private List<Produto> atualizaGrupoMargem (List < GrupoProduto > grupoProdutos, BigDecimal valor,
        boolean isRelativo){
            grupoProdutos = grupoProdutos.stream().map(grupoProduto -> {
                if (isRelativo) {
                    testeNegativo(grupoProduto.getMargemLucro().add(valor));
                    testePositivo(grupoProduto.getMargemLucro().add(valor));
                    grupoProduto.setMargemLucro(grupoProduto.getMargemLucro().add(valor));
                } else {
                    testeNegativo(valor);
                    testePositivo(valor);
                    grupoProduto.setMargemLucro(valor);
                }
                return grupoProduto;
            }).toList(); //

            grupoProdutoRepository.saveAll(grupoProdutos);
            var produtos = grupoProdutos.stream().map(grupoProduto -> grupoProduto.getProdutos()).flatMap(List::stream).collect(Collectors.toList());
            produtos.forEach(Produto::calculateValorVenda);
            return produtoRepository.saveAll(produtos);


        }

        private void testeNegativo (BigDecimal valor){
            if (valor.compareTo(BigDecimal.ZERO) == -1) {
                throw new InvalidMargemException("Valor não pode ser negativo");
            }
        }
        private void testePositivo (BigDecimal margem){
            if (margem.compareTo(BigDecimal.valueOf(100)) == 1) {
                throw new InvalidMargemException("Valor da Margem não pode ser maior que 100%");
            }
        }

        private BigDecimal percent (BigDecimal valor){
            return valor.divide(BigDecimal.valueOf(100));
        }
    }


