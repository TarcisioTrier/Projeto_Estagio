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
import triersistemas.estagio_back_end.dto.request.FilialPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.Orderer;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.QFilial;
import triersistemas.estagio_back_end.repository.FilialRepositoryCustom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilialRepositoryImpl implements FilialRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    final QFilial filial = QFilial.filial;

    @Override
    public Page<FilialResponseDto> buscarFiliais(FilialPagedRequestDto filialDto, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        List<OrderSpecifier<?>> sort = new ArrayList<>();
        JPAQuery<FilialResponseDto> query = new JPAQuery<>(em);
        Optional.ofNullable(filialDto).ifPresent(dto -> {
            Optional.ofNullable(dto.nomeFantasia()).ifPresent(nomeFantasia -> builder.and(mapper(dto.filter().get("nomeFantasia"), "nomeFantasia", nomeFantasia)));
            Optional.ofNullable(dto.cnpj()).ifPresent(cnpj -> builder.and(mapper(dto.filter().get("cnpj"), "cnpj", cnpj)));
            Optional.ofNullable(dto.telefone()).ifPresent(telefone -> builder.and(mapper(dto.filter().get("telefone"), "telefone", telefone)));
            Optional.ofNullable(dto.email()).ifPresent(email -> builder.and(mapper(dto.filter().get("email"), "email", email)));
            Optional.ofNullable(dto.orderer()).ifPresent(orders ->
                    orders.forEach(filialOrd -> {
                        sort.add(createOrderSpecifier(filialOrd));
                    })
            );
        });
        List<FilialResponseDto> filiais = query.select(Projections.constructor(FilialResponseDto.class, filial)).from(filial)
                .where(builder)
                .orderBy(sort.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.from(filial).where(builder).fetchCount();

        return new PageImpl<>(filiais, pageable, total);
    }

    private Predicate mapper(String matchMode, String item, String field) {
        var path = Expressions.stringPath(filial, item);
        switch (matchMode) {
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
            return new OrderSpecifier<>(Order.ASC, Expressions.stringPath(filial, order.getField()));
        } else {
            return new OrderSpecifier<>(Order.DESC, Expressions.stringPath(filial, order.getField()));
        }

    }

    @Override
    public Page<FilialResponseDto> buscarFiliais(String nome, String cnpj, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (nome != null && !nome.isEmpty()) {
            builder.and(filial.nomeFantasia.containsIgnoreCase(nome));
        }

        if (cnpj != null && !cnpj.isEmpty()) {
            builder.and(filial.cnpj.contains(cnpj));
        }

        JPAQuery<FilialResponseDto> query = new JPAQuery<>(em);

        List<FilialResponseDto> filiais = query.select(Projections.constructor(FilialResponseDto.class, filial))
                .from(filial)
                .where(builder)
                .orderBy(filial.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.from(filial)
                .where(builder)
                .fetchCount();
        return new PageImpl<>(filiais, pageable, total);
    }

    @Override
    public List<FilialResponseDto> buscarFiliais(String nome) {
        BooleanBuilder builder = new BooleanBuilder();

        if (nome != null && !nome.isEmpty()) {
            builder.and(filial.nomeFantasia.containsIgnoreCase(nome));
        }
        JPAQuery<FilialResponseDto> query = new JPAQuery<>(em);
        return query.select(Projections.constructor(FilialResponseDto.class, filial))
                .from(filial)
                .where(builder)
                .orderBy(filial.id.asc())
                .fetch();
    }


}