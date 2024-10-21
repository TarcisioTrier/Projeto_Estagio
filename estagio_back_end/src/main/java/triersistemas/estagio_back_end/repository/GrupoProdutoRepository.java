package triersistemas.estagio_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;

import java.util.List;

@Repository
public interface GrupoProdutoRepository extends JpaRepository<GrupoProduto, Long>, GrupoProdutoRepositoryCustom {
}
