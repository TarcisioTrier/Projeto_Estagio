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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.FornecedorPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.Orderer;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.entity.QFornecedor;
import triersistemas.estagio_back_end.repository.FornecedorRepositoryCustom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FornecedorRepositoryImpl implements FornecedorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    final QFornecedor fornecedor = QFornecedor.fornecedor;


    @Override
    public Page<FornecedorResponseDto> buscarFornecedores(Long filialId, FornecedorPagedRequestDto fornecedorDto, Pageable pageable) {
        JPAQuery<Fornecedor> query = new JPAQuery<>(entityManager);
        List<OrderSpecifier<?>> sort = new ArrayList<>();
        BooleanBuilder builder = new BooleanBuilder();
        Optional.of(filialId).ifPresent(id -> builder.and(fornecedor.filial.id.eq(id)));
        Optional.ofNullable(fornecedorDto).ifPresent(dto -> {
            Optional.ofNullable(dto.nomeFantasia()).ifPresent(nomeFantasia -> builder.and(mapper(dto.filter().get("nomeFantasia"), "nomeFantasia", nomeFantasia)));
            Optional.ofNullable(dto.razaoSocial()).ifPresent(razaoSocial -> builder.and(mapper(dto.filter().get("razaoSocial"), "razaoSocial", razaoSocial)));
            Optional.ofNullable(dto.cnpj()).ifPresent(cnpj -> builder.and(mapper(dto.filter().get("cnpj"), "cnpj", cnpj)));
            Optional.ofNullable(dto.telefone()).ifPresent(telefone -> builder.and(mapper(dto.filter().get("telefone"), "telefone", telefone)));
            Optional.ofNullable(dto.email()).ifPresent(email -> builder.and(mapper(dto.filter().get("email"), "email", email)));
            Optional.ofNullable(dto.orderer()).ifPresent(orders ->
                    orders.forEach(fornecedorOrd -> {
                        sort.add(createOrderSpecifier(fornecedorOrd));
                    })
            );
        });

        List<FornecedorResponseDto> fornecedores = query.select(Projections.constructor(FornecedorResponseDto.class, fornecedor)).from(fornecedor)
                .where(builder)
                .orderBy(sort.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = query.from(fornecedor).where(builder).fetchCount();

        return new PageImpl<>(fornecedores, pageable, total);
    }

    private OrderSpecifier<?> createOrderSpecifier(Orderer order) {
        if (order.getOrder()>0){
            return new OrderSpecifier<>(Order.ASC, Expressions.stringPath(fornecedor, order.getField()));
        }else{
            return new OrderSpecifier<>(Order.DESC, Expressions.stringPath(fornecedor, order.getField()));
        }
    }

    private Predicate mapper(String matchMode, String item, String field) {
        var path = Expressions.stringPath(fornecedor, item);
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
    public List<FornecedorResponseDto> buscarFornecedores(String nome, Long filialId) {
        JPAQuery<Fornecedor> query = new JPAQuery<>(entityManager);

        BooleanBuilder builder = new BooleanBuilder();

        if (nome != null && !nome.isEmpty()) {
            builder.and(fornecedor.nomeFantasia.containsIgnoreCase(nome));
        }
        if (filialId != null) {
            builder.and(fornecedor.filial.id.eq(filialId));
        }
        return query.select(Projections.constructor(FornecedorResponseDto.class, fornecedor)).from(fornecedor)
                .where(builder).fetch();
    }


}
