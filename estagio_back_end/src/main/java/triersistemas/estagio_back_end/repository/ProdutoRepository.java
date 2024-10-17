package triersistemas.estagio_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import triersistemas.estagio_back_end.entity.Produto;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoRepositoryCustom {
    Optional<Produto> getProdutoByCodigoBarras(String codigoBarras);
}
