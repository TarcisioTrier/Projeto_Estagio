package triersistemas.estagio_back_end.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.entity.QGrupoProduto;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepositoryCustom;

import java.util.List;

public class GrupoProdutoRepositoryImpl implements GrupoProdutoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    final QGrupoProduto grupoProduto = QGrupoProduto.grupoProduto;


    @Override
    public Page<GrupoProdutoResponseDto> buscarGrupoProduto(String nomeGrupo, TipoGrupoProduto tipoGrupo, Long idFilial, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (nomeGrupo != null && !nomeGrupo.isEmpty()) {
            builder.and(grupoProduto.nomeGrupo.containsIgnoreCase(nomeGrupo));
        }
        if (idFilial != null) {
            builder.and(grupoProduto.filial.id.eq(idFilial));
        }

        if (tipoGrupo != null ) {
            builder.and(grupoProduto.tipoGrupo.eq(tipoGrupo));
        }
        JPAQuery<GrupoProdutoResponseDto> query = new JPAQuery<>(em);
        List<GrupoProdutoResponseDto> gruposProduto = query.select(Projections.constructor(GrupoProdutoResponseDto.class,grupoProduto))
                .from(grupoProduto)
                .where(builder)
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
                .where(grupoProduto.atualizaPreco.eq(true))
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
