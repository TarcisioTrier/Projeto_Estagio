package triersistemas.estagio_back_end.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.QFilial;
import triersistemas.estagio_back_end.repository.FilialRepositoryCustom;

import java.util.List;

public class FilialRepositoryImpl implements FilialRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    final QFilial filial = QFilial.filial;

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

        // Conta o total de registros
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