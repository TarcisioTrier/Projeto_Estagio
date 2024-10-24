package triersistemas.estagio_back_end.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
import triersistemas.estagio_back_end.entity.QGrupoProduto;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepositoryCustom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GrupoProdutoRepositoryImpl implements GrupoProdutoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    final QGrupoProduto grupoProduto = QGrupoProduto.grupoProduto;
    private OrderSpecifier<?> createOrderSpecifier(Orderer order) {
        if (order.getOrder()>0){
            return new OrderSpecifier<>(Order.ASC, Expressions.stringPath(grupoProduto, order.getField()));
        }else{
            return new OrderSpecifier<>(Order.DESC, Expressions.stringPath(grupoProduto, order.getField()));
        }
    }

    @Override
    public Page<GrupoProdutoResponseDto> buscarGrupoProduto(GrupoProdutoPagedRequestDto grupoProdutoDto, Long filialId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        List<OrderSpecifier<?>> sort = new ArrayList<>();
        Optional.of(filialId).ifPresent(id -> builder.and(grupoProduto.filial.id.eq(id)));
        Optional.ofNullable(grupoProdutoDto).ifPresent(dto -> {
            Optional.ofNullable(dto.nomeGrupo()).ifPresent(nomeGrupo -> builder.and(grupoProduto.nomeGrupo.containsIgnoreCase(nomeGrupo)));
            Optional.ofNullable(dto.tipoGrupo()).ifPresent(tipoGrupoProduto -> builder.and(grupoProduto.tipoGrupo.eq(tipoGrupoProduto)));
            Optional.ofNullable(dto.atualizaPreco()).ifPresent(atualizaPreco -> builder.and(grupoProduto.atualizaPreco.eq(atualizaPreco)));
            Optional.ofNullable(dto.situacaoCadastro()).ifPresent(situacaoCadastro -> builder.and(grupoProduto.situacaoCadastro.eq(situacaoCadastro)));
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
}
