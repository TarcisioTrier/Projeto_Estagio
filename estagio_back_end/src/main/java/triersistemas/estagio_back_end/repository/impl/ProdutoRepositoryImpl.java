package triersistemas.estagio_back_end.repository.impl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.Filter;
import triersistemas.estagio_back_end.dto.request.Orderer;
import triersistemas.estagio_back_end.dto.request.PagedRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.QProduto;
import triersistemas.estagio_back_end.repository.ProdutoRepositoryCustom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    final QProduto produto = QProduto.produto;

    @Override
    public Page<ProdutoResponseDto> buscarProduto(PagedRequestDto pagedDto, Long filialId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        List<OrderSpecifier<?>> sort = new ArrayList<>();
        Optional.of(filialId).ifPresent(id -> builder.and(produto.filial.id.eq(id)));
        Optional.ofNullable(pagedDto).ifPresent(dto -> {
            Optional.ofNullable(dto.filter()).ifPresent(filters -> {
                filters.forEach(filter -> builder.and(verifyFilter(filter)));});
            Optional.ofNullable(dto.orderer()).ifPresent(orders ->{
                        if(orders.size()>0){
                            orders.forEach(produtoOrd -> {
                                sort.add(createOrderSpecifier(produtoOrd));
                            });
                        }
            }


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

    private OrderSpecifier<?> createOrderSpecifier(Orderer order) {
        if (order.getOrder()>0){
            return new OrderSpecifier<>(Order.ASC, Expressions.stringPath(produto, order.getField()));
        }else{
            return new OrderSpecifier<>(Order.DESC, Expressions.stringPath(produto, order.getField()));
        }
    }

    private Predicate verifyFilter(Filter filter){
        switch(filter.getMatchMode()){
            case "startsWith":
                return Expressions.stringPath(produto, filter.getField()).startsWith(filter.getValue());
            case "endsWith":
                return Expressions.stringPath(produto, filter.getField()).endsWith(filter.getValue());
            case "contains":
                return Expressions.stringPath(produto, filter.getField()).contains(filter.getValue());
            case "equals":
                return Expressions.stringPath(produto, filter.getField()).eq(filter.getValue());
                case "notContains":
                    return Expressions.stringPath(produto, filter.getField()).contains(filter.getValue()).isFalse();
            case "notEquals":
                return Expressions.stringPath(produto, filter.getField()).ne(filter.getValue());
            default:
                return null;
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
