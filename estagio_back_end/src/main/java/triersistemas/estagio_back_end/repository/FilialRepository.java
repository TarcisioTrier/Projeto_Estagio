package triersistemas.estagio_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import triersistemas.estagio_back_end.entity.Filial;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {
}
