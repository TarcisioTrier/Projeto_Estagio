package triersistemas.estagio_back_end.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.entity.QFornecedor;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.repository.FornecedorRepositoryCustom;

import java.util.List;
import java.util.stream.Collectors;

public class FornecedorRepositoryImpl implements FornecedorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    final QFornecedor fornecedor = QFornecedor.fornecedor;

    @Override
    public Page<FornecedorResponseDto> buscarFornecedores(String nome, String cnpj, SituacaoCadastro situacaoCadastro, Pageable pageable) {
        JPAQuery<Fornecedor> query = new JPAQuery<>(entityManager);

        BooleanBuilder builder = new BooleanBuilder();

        if (nome != null && !nome.isEmpty()) {
            builder.and(fornecedor.nomeFantasia.containsIgnoreCase(nome));
        }
        if (cnpj != null && !cnpj.isEmpty()) {
            builder.and(fornecedor.cnpj.eq(cnpj));
        }
        if (situacaoCadastro != null) {
            builder.and(fornecedor.situacaoCadastro.eq(situacaoCadastro));
        }

        query.from(fornecedor)
                .where(builder);

        long total = query.fetchCount();

        List<Fornecedor> fornecedores = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<FornecedorResponseDto> fornecedoresDto = fornecedores.stream()
                .map(FornecedorResponseDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(fornecedoresDto, pageable, total);
    }
}
