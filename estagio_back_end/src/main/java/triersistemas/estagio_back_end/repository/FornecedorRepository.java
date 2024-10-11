package triersistemas.estagio_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import triersistemas.estagio_back_end.entity.Fornecedor;

import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long>, FornecedorRepositoryCustom {

    Optional<Fornecedor> findByCnpj(String cnpj);
}
