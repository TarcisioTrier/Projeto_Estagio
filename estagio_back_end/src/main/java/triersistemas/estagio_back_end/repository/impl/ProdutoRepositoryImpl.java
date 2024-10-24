package triersistemas.estagio_back_end.repository.impl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import triersistemas.estagio_back_end.dto.request.Orderer;
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.QProduto;
import triersistemas.estagio_back_end.repository.ProdutoRepositoryCustom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    final QProduto produto = QProduto.produto;

    @Override
    public Page<ProdutoResponseDto> buscarProduto(ProdutoPagedRequestDto produtoPagedDto, Long filialId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        List<OrderSpecifier<?>> sort = new ArrayList<>();
        if (filialId != null) {
            builder.and(produto.filial.id.eq(filialId));
        }
        Optional.ofNullable(produtoPagedDto).ifPresent(dto -> {
            Optional.ofNullable(dto.nome()).ifPresent(nome -> builder.and(produto.nome.containsIgnoreCase(nome)));
            Optional.ofNullable(dto.codigoBarras()).ifPresent(codigoBarras -> builder.and(produto.codigoBarras.containsIgnoreCase(codigoBarras)));
            Optional.ofNullable(dto.situacaoCadastro()).ifPresent(situacaoCadastro -> builder.and(produto.situacaoCadastro.eq(situacaoCadastro)));
            Optional.ofNullable(dto.tipoProduto()).ifPresent(tipo -> builder.and(produto.tipoProduto.eq(tipo)));
            Optional.ofNullable(dto.valorProduto()).ifPresent(valor -> builder.and(produto.valorProduto.eq(valor)));
            Optional.ofNullable(dto.atualizaPreco()).ifPresent(atualizaPreco -> builder.and(produto.atualizaPreco.eq(atualizaPreco)));
            Optional.ofNullable(dto.margemLucro()).ifPresent(margemLucro -> builder.and(produto.margemLucro.eq(margemLucro)));
            Optional.ofNullable(dto.descricao()).ifPresent(descricao -> builder.and(produto.descricao.containsIgnoreCase(descricao)));
            Optional.ofNullable(dto.apresentacao()).ifPresent(apresentacao -> builder.and(produto.apresentacao.eq(apresentacao)));
            Optional.ofNullable(dto.valorVenda()).ifPresent(valorVenda -> builder.and(produto.valorVenda.eq(valorVenda)));
            Optional.ofNullable(dto.dataUltimaAtualizacaoPreco()).ifPresent(data -> builder.and(produto.dataUltimaAtualizacaoPreco.eq(data)));
            Optional.ofNullable(dto.orderer()).ifPresent(orders ->
                    orders.forEach(produtoOrd -> {

                        // Determine the order (ascending or descending)
                        var orderSpecifier = createOrderSpecifier(produtoOrd);

                        // Add the orderSpecifier to the sort object
                        sort.add(orderSpecifier);
                    })
            );

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

    public OrderSpecifier<?> createOrderSpecifier(Orderer order) {
        if (order.getOrder()>0){
            return new OrderSpecifier<>(Order.ASC, Expressions.stringPath(produto, order.getField()));
        }else{
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
