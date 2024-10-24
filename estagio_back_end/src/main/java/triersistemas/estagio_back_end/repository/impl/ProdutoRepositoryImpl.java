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
import triersistemas.estagio_back_end.entity.QProduto;
import triersistemas.estagio_back_end.repository.ProdutoRepositoryCustom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    final QProduto produto = QProduto.produto;

    @Override
    public Page<ProdutoResponseDto> buscarProduto(ProdutoPagedRequestDto pagedDto, Long filialId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        List<OrderSpecifier<?>> sort = new ArrayList<>();
        Optional.of(filialId).ifPresent(id -> builder.and(produto.filial.id.eq(id)));
        Optional.ofNullable(pagedDto).ifPresent(dto -> {
            Optional.ofNullable(dto.nome()).ifPresent(nome-> builder.and(mapperString(dto.filter().get("nome"), "nome", nome)));
            Optional.ofNullable(dto.orderer()).ifPresent(orders ->{
                        if(orders.size()>0){ orders.forEach(produtoOrd -> sort.add(createOrderSpecifier(produtoOrd)));}});

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

    private Predicate mapperString(String matchMode, String item, String field) {
        var path = Expressions.stringPath(produto, item);
        switch(matchMode){
            case "startsWith":
                return path.startsWith(field);
                break;
            case:

        }
    }


    private OrderSpecifier<?> createOrderSpecifier(Orderer order) {
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
