package triersistemas.estagio_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import triersistemas.estagio_back_end.entity.Filial;

import java.util.Optional;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long>, FilialRepositoryCustom {

    boolean existsByCnpj(String cnpj);

    Optional<Filial> findByCnpj(String cnpj);
}
