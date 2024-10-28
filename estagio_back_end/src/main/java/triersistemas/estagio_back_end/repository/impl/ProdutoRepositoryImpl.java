package triersistemas.estagio_back_end.repository.impl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.Orderer;
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.entity.QProduto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.repository.ProdutoRepositoryCustom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    final QProduto produto = QProduto.produto;

    @Override
    public List<Produto> buscarProduto(ProdutoPagedRequestDto pagedDto, Long filialId) {
        BooleanBuilder builder = new BooleanBuilder();
        Optional.of(filialId).ifPresent(id -> builder.and(produto.filial.id.eq(id)));
        Optional.ofNullable(pagedDto).ifPresent(dto -> {
            Optional.ofNullable(dto.nome()).ifPresent(nome -> builder.and(mapper(dto.filter().get("nome"), "nome", nome)));
            Optional.ofNullable(dto.codigoBarras()).ifPresent(codigoBarras -> builder.and(mapper(dto.filter().get("codigoBarras"), "codigoBarras", codigoBarras)));
            Optional.ofNullable(dto.descricao()).ifPresent(descricao -> builder.and(mapper(dto.filter().get("descricao"), "descricao", descricao)));
            Optional.ofNullable(dto.tipoProduto()).ifPresent(tipoProduto -> builder.and(mapper("tipoProduto", tipoProduto)));
            Optional.ofNullable(dto.apresentacao()).ifPresent(apresentacao -> builder.and(mapper("apresentacao", apresentacao)));
            Optional.ofNullable(dto.margemLucro()).ifPresent(margemLucro -> builder.and(mapper(dto.filter().get("margemLucro"),"margemLucro", margemLucro)));
            Optional.ofNullable(dto.atualizaPreco()).ifPresent(atualizaPreco -> builder.and(produto.atualizaPreco.eq(atualizaPreco)));
            Optional.ofNullable(dto.valorProduto()).ifPresent(valorProduto -> builder.and(mapper(dto.filter().get("valorProduto"), "valorProduto", valorProduto)));
            Optional.ofNullable(dto.valorVenda()).ifPresent(valorVenda -> builder.and(mapper(dto.filter().get("valorVenda"), "valorVenda", valorVenda)));
        });
        JPAQuery<Produto> query = new JPAQuery<>(em);
        return query.from(produto)
                .where(builder)
                .fetch();
    }

    @Override
    public Page<ProdutoResponseDto> buscarProduto(ProdutoPagedRequestDto pagedDto, Long filialId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        List<OrderSpecifier<?>> sort = new ArrayList<>();
        Optional.of(filialId).ifPresent(id -> builder.and(produto.filial.id.eq(id)));
        Optional.ofNullable(pagedDto).ifPresent(dto -> {
            Optional.ofNullable(dto.nome()).ifPresent(nome -> builder.and(mapper(dto.filter().get("nome"), "nome", nome)));
            Optional.ofNullable(dto.codigoBarras()).ifPresent(codigoBarras -> builder.and(mapper(dto.filter().get("codigoBarras"), "codigoBarras", codigoBarras)));
            Optional.ofNullable(dto.descricao()).ifPresent(descricao -> builder.and(mapper(dto.filter().get("descricao"), "descricao", descricao)));
            Optional.ofNullable(dto.tipoProduto()).ifPresent(tipoProduto -> builder.and(mapper("tipoProduto", tipoProduto)));
            Optional.ofNullable(dto.apresentacao()).ifPresent(apresentacao -> builder.and(mapper("apresentacao", apresentacao)));
            Optional.ofNullable(dto.margemLucro()).ifPresent(margemLucro -> builder.and(mapper(dto.filter().get("margemLucro"),"margemLucro", margemLucro)));
            Optional.ofNullable(dto.atualizaPreco()).ifPresent(atualizaPreco -> builder.and(produto.atualizaPreco.eq(atualizaPreco)));
            Optional.ofNullable(dto.valorProduto()).ifPresent(valorProduto -> builder.and(mapper(dto.filter().get("valorProduto"), "valorProduto", valorProduto)));
            Optional.ofNullable(dto.valorVenda()).ifPresent(valorVenda -> builder.and(mapper(dto.filter().get("valorVenda"), "valorVenda", valorVenda)));

            Optional.ofNullable(dto.orderer()).ifPresent(orders -> {
                if (orders.size() > 0) {
                    orders.forEach(produtoOrd -> sort.add(createOrderSpecifier(produtoOrd)));
                }
            });

        });


        JPAQuery<ProdutoResponseDto> query = new JPAQuery<>(em);
        List<ProdutoResponseDto> produtos = query.select(Projections.constructor(ProdutoResponseDto.class, produto))
                .from(produto)
                .where(builder)
                .orderBy(sort.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = query.from(produto)
                .where(builder)
                .fetchCount();
        return new PageImpl<>(produtos, pageable, total);
    }



    private Predicate mapper(String matchMode, String item, BigDecimal field) {
        var path = Expressions.numberPath(BigDecimal.class, produto, item);
        switch(matchMode) {
            case "lt":
                return path.lt(field);
            case "lte":
                return path.lt(field).or(path.eq(field));
            case "gt":
                return path.gt(field);
            case "gte":
                return path.gt(field).or(path.eq(field));
            case "notEquals":
                return path.eq(field).not();
            default:
                return path.eq(field);

        }
    }

    private Predicate mapper(String item, Apresentacao field) {
        var path = Expressions.enumPath(Apresentacao.class, produto, item);
        return path.eq(field);
    }

    private Predicate mapper( String item, TipoProduto field) {
        var path = Expressions.enumPath(TipoProduto.class, produto, item);
        return path.eq(field);
    }

    private Predicate mapper(String matchMode, String item, String field) {
        var path = Expressions.stringPath(produto, item);
        switch(matchMode) {
            case "startsWith":
                return path.startsWith(field);
            case "contains":
                return path.contains(field);
            case "endsWith":
                return path.endsWith(field);
            case "notContains":
                return path.contains(field).not();
            case "notEquals":
                return path.eq(field).not();
            default:
                return path.eq(field);

        }
    }
    private OrderSpecifier<?> createOrderSpecifier(Orderer order) {
        if (order.getOrder() > 0) {
            return new OrderSpecifier<>(Order.ASC, Expressions.stringPath(produto, order.getField()));
        } else {
            return new OrderSpecifier<>(Order.DESC, Expressions.stringPath(produto, order.getField()));
        }
    }


    @Override
    public List<ProdutoResponseDto> getAllProdutoAlteraPreco() {
        JPAQuery<ProdutoResponseDto> query = new JPAQuery<>(em);

        return query.select(Projections.constructor(ProdutoResponseDto.class, produto))
                .from(produto)
                .where(produto.atualizaPreco.isTrue())
                .fetch();
    }


}
