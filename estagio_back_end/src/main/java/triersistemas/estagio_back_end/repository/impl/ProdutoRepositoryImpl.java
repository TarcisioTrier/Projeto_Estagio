package triersistemas.estagio_back_end.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.QProduto;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.repository.ProdutoRepositoryCustom;

import java.util.List;

public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    final QProduto produto = QProduto.produto;

    @Override
    public Page<ProdutoResponseDto> buscarProduto(String nome, TipoProduto tipo, Long grupoProdutoId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (nome != null && !nome.isEmpty()) {
            builder.and(produto.nome.containsIgnoreCase(nome));
        }
        if (grupoProdutoId != null) {
            builder.and(produto.grupoProduto.id.eq(grupoProdutoId));
        }
        if (tipo != null ) {
            builder.and(produto.tipoProduto.eq(tipo));
        }
        JPAQuery<ProdutoResponseDto> query = new JPAQuery<>(em);
        List<ProdutoResponseDto> produtos = query.select(Projections.constructor(ProdutoResponseDto.class,produto))
                .from(produto)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = query.from(produto)
                .where(builder)
                .fetchCount();
        return new PageImpl<>(produtos, pageable, total);
    }
}
