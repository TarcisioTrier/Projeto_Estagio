package triersistemas.estagio_back_end.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.dto.request.FilialPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialChartDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.*;
import triersistemas.estagio_back_end.enuns.SituacaoContrato;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.validators.CnpjValidator;
import triersistemas.estagio_back_end.validators.EnderecosValidator;
import triersistemas.estagio_back_end.validators.FoneValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilialServiceImplTest {

    @Mock
    private FilialRepository filialRepository;

    @Mock
    private EnderecosValidator enderecosValidator;

    @Mock
    private CnpjValidator cnpjValidator;

    @Mock
    private FoneValidator foneValidator;

    @InjectMocks
    private FilialServiceImpl filialService;

    private Filial filial;
    private Produto produto;
    private Fornecedor fornecedor;
    private GrupoProduto grupoProduto;
    private Enderecos enderecos;
    private EnderecosDto enderecosDto;
    private FilialRequestDto requestDto;
    private final Long filialId = 1L;

    @Captor
    private ArgumentCaptor<Filial> filialArgumentCaptor;

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
                "52.353.295/0001-83",
                "(63) 3245-6887",
                "email@email.com",
                SituacaoContrato.ATIVO,
                enderecosDto);

        filial = new Filial(requestDto);
        filial.setId(filialId);
        enderecos = new Enderecos(requestDto.endereco());

    }

    @Nested
    class addFilialTest {

        @Test
        @DisplayName("Deve retornar um FilialResponse e salvar no banco com os dados preenchidos")
        void addFilialTest_V1() {

            doReturn(filial).when(filialRepository).save(filialArgumentCaptor.capture());
            doReturn(enderecos).when(enderecosValidator).validateEndereco(requestDto.endereco());
            doNothing().when(foneValidator).validateFone(requestDto.telefone());
            doNothing().when(cnpjValidator).validateCnpjPostFilial(requestDto.cnpj());

            var actual = filialService.addFilial(requestDto);

            var capturedFilial = filialArgumentCaptor.getValue();
            capturedFilial.setId(filialId);

            var expected = new FilialResponseDto(capturedFilial);

            assertNotNull(actual);
            assertEquals(expected, actual);

            verify(filialRepository, times(1)).save(filialArgumentCaptor.capture());
            verify(enderecosValidator, times(1)).validateEndereco(requestDto.endereco());
            verify(foneValidator, times(1)).validateFone(requestDto.telefone());
            verify(cnpjValidator, times(1)).validateCnpjPostFilial(requestDto.cnpj());
        }
    }

    @Nested
    class updateFilialTest {

        @Test
        @DisplayName("Deve retornar um FilialResponse e atualizar os dados no banco")
        void updateFilialTest_V1() {

            var newRequestDto = new FilialRequestDto(
                    "NomeUpdate",
                    "RazãoUpdate",
                    "52.353.295/0001-83",
                    "(63) 3245-6887",
                    "emailUpdate@email.com",
                    SituacaoContrato.ATIVO,
                    enderecosDto);

            doReturn(filial).when(filialRepository).save(filialArgumentCaptor.capture());
            doReturn(Optional.of(filial)).when(filialRepository).findById(filialId);

            var actual = filialService.updateFilial(filialId, newRequestDto);
            var expected = new FilialResponseDto(filialArgumentCaptor.getValue());

            assertNotNull(actual);
            assertEquals(expected, actual);

            verify(filialRepository, times(1)).save(filial);
            verify(filialRepository, times(1)).findById(filialId);
        }

        @Test
        @DisplayName("Deve retornar uma filialResponse e atualizar os dados com cnpj diferente")
        void updateFilialTest_V2() {

            var newRequestDto = new FilialRequestDto(
                    "NomeUpdate",
                    "RazãoUpdate",
                    "12.345.678/0001-90",
                    "(63) 3245-6887",
                    "emailUpdate@email.com",
                    SituacaoContrato.ATIVO,
                    enderecosDto);

            doReturn(Optional.of(filial)).when(filialRepository).findById(filialId);
            doReturn(filial).when(filialRepository).save(filialArgumentCaptor.capture());

            var actual = filialService.updateFilial(filialId, newRequestDto);

            var expected = new FilialResponseDto(filialArgumentCaptor.getValue());

            assertNotNull(actual);
            assertEquals(expected, actual);

            verify(cnpjValidator, times(1)).validateCnpjUpdateFilial(newRequestDto.cnpj(), filial.getId());
            verify(filialRepository, times(1)).save(filial);
            verify(filialRepository, times(1)).findById(filialId);
        }

    }

    @Nested
    class deleteFilialTest {

        @Test
        @DisplayName("Deve deletar e retornar uma filialResponse encontrada pelo id ")
        void deleteFilalTest_V1() {

            doReturn(Optional.of(filial)).when(filialRepository).findById(filialId);
            doNothing().when(filialRepository).delete(filialArgumentCaptor.capture());

            var actual = filialService.deleteFilial(filialId);
            var expected = filialArgumentCaptor.getValue();

            assertNotNull(actual);
            assertEquals(expected.getId(), actual.id());

            verify(filialRepository, times(1)).findById(filialId);
            verify(filialRepository, times(1)).delete(filial);
        }
    }

    @Nested
    class getFilialByIdTest {

        @Test
        @DisplayName("Deve retornar uma filial cadastrada pelo Id")
        void getFilialByIdTest_V1() {

            var expected = new FilialResponseDto(filial);

            doReturn(Optional.of(filial)).when(filialRepository).findById(filialId);

            var actual = filialService.getFilialById(filialId);

            assertEquals(expected, actual);

            verify(filialRepository, times(1)).findById(filialId);
        }
    }

    @Nested
    class getAllFilialTest {

        @Test
        @DisplayName("Deve retornar todas filiais cadastradas")
        void getAllFilialTest_V1() {

            List<FilialResponseDto> expected = new ArrayList<>();
            expected.add(new FilialResponseDto(filial));

            doReturn(List.of(filial)).when(filialRepository).findAll();

            var actual = filialService.getAllFilial();

            assertEquals(expected, actual);
            verify(filialRepository, times(1)).findAll();
        }
    }
    @Nested
    class getChartTest{
        @Test
        @DisplayName("Deve retornar a Lista das informações para criar os graficos")
        void getChartTest_V1(){
            produto = mock();
            grupoProduto = mock();
            fornecedor = mock();
            filial = mock();
            var filiais = List.of(filial);
            var produtos = List.of(produto);
            var gruposProduto = List.of(grupoProduto);
            var fornecedores = List.of(fornecedor);
            doReturn(filiais).when(filialRepository).findAll();
            doReturn(produtos).when(filial).getProdutos();
            doReturn(gruposProduto).when(filial).getGrupoProdutos();
            doReturn(fornecedores).when(filial).getFornecedores();
            doReturn(grupoProduto).when(produto).getGrupoProduto();
            var actual = filialService.getChart();
            assertEquals(filiais.stream().map(FilialChartDto::new).toList(), actual);

        }
        @Test
        @DisplayName("Deve retornar a Lista Nula, pois não tem nenhuma filial no repositorio")
        void getChartTest_v2(){
            var filiais = List.of();
            doReturn(filiais).when(filialRepository).findAll();
            var actual = filialService.getChart();
            assertEquals(List.of(), actual);
        }
    }

    @Nested
    class getFilialPaged{
        @Test
        @DisplayName("Deve retornar a filial paginada")
        void getFilialPaged_v1(){
            FilialPagedRequestDto pagedDto = mock();
            FilialResponseDto responseDto = mock();
            Pageable pageable = mock();
            Page<FilialResponseDto> page = mock();
            doReturn(page).when(filialRepository).buscarFiliais(pagedDto, pageable);
            var actual = filialService.getFilialPaged(pagedDto,pageable);
            assertEquals(page, actual);
        }
    }

    @Nested
    class getFilialFilterTest {

        @Test
        @DisplayName("Deve retornar filiais que batem com o nome dado, em uma lista")
        void getFilialFilterTest_V2() {
            String name = "Nome";

            FilialResponseDto responseDto = new FilialResponseDto(filial);
            List<FilialResponseDto> expected = List.of(responseDto);

            doReturn(List.of(responseDto)).when(filialRepository).buscarFiliais(name);

            List<FilialResponseDto> actual = filialService.getFilialFilter(name);

            assertEquals(expected.getFirst(), actual.getFirst());
            assertEquals(name, actual.getFirst().nomeFantasia());

            verify(filialRepository, times(1)).buscarFiliais(name);
        }

    }
}