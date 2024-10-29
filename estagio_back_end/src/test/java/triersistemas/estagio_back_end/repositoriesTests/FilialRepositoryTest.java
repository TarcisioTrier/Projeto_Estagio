package triersistemas.estagio_back_end.repositoriesTests;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.repository.FilialRepository;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class FilialRepositoryTest {

    @Autowired
    FilialRepository filialRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("Deve retornar filiais pelo cnpj")
    void findByCnpjSucess() {
        String testCnpjSucess = "99999999999999";
        FilialRequestDto requestDto = new FilialRequestDto("Test", "Test", "99999999999999", "999999999", "test@test.com", null, null);


        Filial filial = createFilial(requestDto);
        Optional<Filial> filialEncontrado = filialRepository.findByCnpj(testCnpjSucess);

        assertNotNull(filialEncontrado);
        assertThat(filialEncontrado.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Não deve retornar filiais pelo cnpj vazio")
    void findByCnpjFailure() {
        String testCnpjSucess = "99999999999999";

        Optional<Filial> filialEncontrado = filialRepository.findByCnpj(testCnpjSucess);

        assertNotNull(filialEncontrado);
        assertThat(filialEncontrado.isEmpty()).isTrue();
    }


    private Filial createFilial(FilialRequestDto request) {
        Filial filial = new Filial(request);
        this.em.persist(filial);
        return filial;
    }
}