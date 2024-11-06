package triersistemas.estagio_back_end.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.dto.request.FilialPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.request.Orderer;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.enuns.SituacaoContrato;
import triersistemas.estagio_back_end.repository.FilialRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FilialRepositoryImplTest {

    @Autowired
    private FilialRepository filialRepository;

    private FilialRequestDto requestDto;

    private FilialRequestDto requestDto2;

    private EnderecosDto enderecosDto;

    @BeforeEach
    void setUp() {
        enderecosDto = new EnderecosDto(
                "Rua Marcos Albino Rafael",
                null,
                null,
                "João Pessoa",
                "PB",
                "58065-156",
                "Planalto Boa Esperança");

        requestDto = new FilialRequestDto(
                "Nome",
                "Razão",
                "52.130.577/0001-11",
                "(45) 2012-3466",
                "email@email.com",
                SituacaoContrato.ATIVO,
                enderecosDto);

        requestDto2 = new FilialRequestDto(
                "Nome 2",
                "Razão 2",
                "07.753.745/0001-701",
                "(67) 3608-8747",
                "email2@email2.com",
                SituacaoContrato.ATIVO,
                enderecosDto);
    }

    @Nested
    class buscarFiliaisTest{

        @Test
        @DisplayName("Deve retornar filiais com filtro paginado")
        void buscarFiliaisTest_V1() {
            Pageable pageable = PageRequest.of(0, 10);

            Filial filial1 = new Filial(requestDto);
            Filial filial2 = new Filial(requestDto2);

            filialRepository.save(filial1);
            filialRepository.save(filial2);


            FilialPagedRequestDto requestDtoPaged = new FilialPagedRequestDto(
                    "Nome 2",
                    "Razão",
                    null,
                    null,
                    null,
                    null,
                    null,
                    List.of(new Orderer("nomeFantasia", 1)),
                    Map.of("nomeFantasia", "contains")
            );

            Page<FilialResponseDto> resultPage = filialRepository.buscarFiliais(requestDtoPaged, pageable);

            assertNotNull(resultPage);
            assertEquals(1, resultPage.getTotalElements());
            assertEquals("Nome 2", resultPage.getContent().getFirst().nomeFantasia());
        }

        @Test
        @DisplayName("Deve retornar filiais com filtro paginado")
        void buscarFiliaisTest_V2() {
            var cnpj ="07.753.745/0001-701";
            Pageable pageable = PageRequest.of(0, 10);

            Filial filial1 = new Filial(requestDto);
            Filial filial2 = new Filial(requestDto2);

            filialRepository.save(filial1);
            filialRepository.save(filial2);


            Page<FilialResponseDto> resultPage = filialRepository.buscarFiliais(null,cnpj, pageable);

            assertNotNull(resultPage);
            assertEquals(1, resultPage.getTotalElements());
            assertEquals("Nome 2", resultPage.getContent().getFirst().nomeFantasia());
        }
    }

}