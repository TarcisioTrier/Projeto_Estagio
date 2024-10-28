package triersistemas.estagio_back_end.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.Orderer;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.QGrupoProduto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepositoryCustom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GrupoProdutoRepositoryImpl implements GrupoProdutoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    final QGrupoProduto grupoProduto = QGrupoProduto.grupoProduto;

    @Override
    public List<GrupoProduto> buscarGrupoProduto(GrupoProdutoPagedRequestDto grupoProdutoDto, Long filialId) {
        BooleanBuilder builder = new BooleanBuilder();
        Optional.of(filialId).ifPresent(id -> builder.and(grupoProduto.filial.id.eq(id)));
        Optional.ofNullable(grupoProdutoDto).ifPresent(dto -> {
            Optional.ofNullable(dto.nomeGrupo()).ifPresent(nomeGrupo -> builder.and(mapper(dto.filter().get("nomeGrupo"), "nomeGrupo", nomeGrupo)));
            Optional.ofNullable(dto.tipoGrupo()).ifPresent(tipoGrupoProduto -> builder.and(mapper("tipoGrupo", tipoGrupoProduto)));
            Optional.ofNullable(dto.atualizaPreco()).ifPresent(atualizaPreco -> builder.and(grupoProduto.atualizaPreco.eq(atualizaPreco)));
            Optional.ofNullable(dto.margemLucro()).ifPresent(margemLucro -> builder.and(mapper(dto.filter().get("margemLucro"),"margemLucro", margemLucro)));
        });
        JPAQuery<GrupoProduto> query = new JPAQuery<>(em);
        return query.from(grupoProduto)
                .where(builder)
                .fetch();
    }

    @Override
    public Page<GrupoProdutoResponseDto> buscarGrupoProduto(GrupoProdutoPagedRequestDto grupoProdutoDto, Long filialId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        List<OrderSpecifier<?>> sort = new ArrayList<>();
        Optional.of(filialId).ifPresent(id -> builder.and(grupoProduto.filial.id.eq(id)));
        Optional.ofNullable(grupoProdutoDto).ifPresent(dto -> {
            Optional.ofNullable(dto.nomeGrupo()).ifPresent(nomeGrupo -> builder.and(mapper(dto.filter().get("nomeGrupo"), "nomeGrupo", nomeGrupo)));
            Optional.ofNullable(dto.tipoGrupo()).ifPresent(tipoGrupoProduto -> builder.and(mapper("tipoGrupo", tipoGrupoProduto)));
            Optional.ofNullable(dto.atualizaPreco()).ifPresent(atualizaPreco -> builder.and(grupoProduto.atualizaPreco.eq(atualizaPreco)));
            Optional.ofNullable(dto.margemLucro()).ifPresent(margemLucro -> builder.and(mapper(dto.filter().get("margemLucro"),"margemLucro", margemLucro)));
            Optional.ofNullable(dto.orderer()).ifPresent(orders ->
                    orders.forEach(grupoProdutoOrd -> {
                        sort.add(createOrderSpecifier(grupoProdutoOrd));
                    })
            );
        });


        JPAQuery<GrupoProdutoResponseDto> query = new JPAQuery<>(em);
        List<GrupoProdutoResponseDto> gruposProduto = query.select(Projections.constructor(GrupoProdutoResponseDto.class,grupoProduto))
                .from(grupoProduto)
                .where(builder)
                .orderBy(sort.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = query.from(grupoProduto)
                .where(builder)
                .fetchCount();
        return new PageImpl<>(gruposProduto, pageable, total);
    }

    private Predicate mapper(String matchMode, String item, BigDecimal field) {
        var path = Expressions.numberPath(BigDecimal.class, grupoProduto, item);
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

    private Predicate mapper( String item, TipoGrupoProduto field) {
        var path = Expressions.enumPath(TipoGrupoProduto.class, grupoProduto, item);
        return path.eq(field);
    }

    private Predicate mapper(String matchMode, String item, String field) {
        var path = Expressions.stringPath(grupoProduto, item);
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


    @Override
    public List<GrupoProdutoResponseDto> getAllGrupoProdutoAlteraPreco() {
        JPAQuery<GrupoProdutoResponseDto> query = new JPAQuery<>(em);

        return query.select(Projections.constructor(GrupoProdutoResponseDto.class, grupoProduto))
                .from(grupoProduto)
                .where(grupoProduto.atualizaPreco.isTrue())
                .fetch();
    }



    @Override
    public List<GrupoProdutoResponseDto> buscarGrupoProduto(String nomeGrupo, Long filialId) {
        BooleanBuilder builder = new BooleanBuilder();
        if (nomeGrupo != null && !nomeGrupo.isEmpty()) {
            builder.and(grupoProduto.nomeGrupo.containsIgnoreCase(nomeGrupo));
        }
        if (filialId != null) {
            builder.and(grupoProduto.filial.id.eq(filialId));
        }
        JPAQuery<GrupoProdutoResponseDto> query = new JPAQuery<>(em);
        List<GrupoProdutoResponseDto> gruposProduto = query.select(Projections.constructor(GrupoProdutoResponseDto.class,grupoProduto))
                .from(grupoProduto)
                .where(builder)
                .fetch();
        return gruposProduto;
    }

    private OrderSpecifier<?> createOrderSpecifier(Orderer order) {
        if (order.getOrder()>0){
            return new OrderSpecifier<>(Order.ASC, Expressions.stringPath(grupoProduto, order.getField()));
        }else{
            return new OrderSpecifier<>(Order.DESC, Expressions.stringPath(grupoProduto, order.getField()));
        }
    }
}
