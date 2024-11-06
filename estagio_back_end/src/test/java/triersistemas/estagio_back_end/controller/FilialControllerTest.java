package triersistemas.estagio_back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.dto.request.FilialPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialChartDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.SituacaoContrato;
import triersistemas.estagio_back_end.services.FilialService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilialController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class FilialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilialService filialService;

    @Autowired
    private ObjectMapper objectMapper;

    private Filial filial;

    private FilialResponseDto responseDto;

    private FilialRequestDto requestDto;

    private EnderecosDto enderecosDto;

    private final Long filialId = 1L;

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
                "email2@email2.com",
                SituacaoContrato.ATIVO,
                enderecosDto);

        filial = new Filial(requestDto);
        filial.setId(filialId);
    }

    @Nested
    class postFilialTest {

        @Test
        @DisplayName("Deve criar uma filial e retornar um ResponseEntity.ok")
        void postFilialTest_V1() throws Exception {
            responseDto = new FilialResponseDto(filial);

            given(filialService.addFilial(ArgumentMatchers.any(FilialRequestDto.class))).willReturn(responseDto);
            ResultActions response = mockMvc.perform(post("/filiais/post")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));

        }
    }

    @Nested
    class updateFilialTest {
        @Test
        @DisplayName("Deve atualizar uma filial e retornar um ResponseEntity.ok")
        void updateFilialTest_V1() throws Exception {
            var requestDtoUpdate = new FilialRequestDto(
                    "Nome Update",
                    "Razão Update",
                    "52.130.577/0001-11",
                    "(45) 2012-3466",
                    "email2@email2.com",
                    SituacaoContrato.ATIVO,
                    enderecosDto
            );
            var responseDto = new FilialResponseDto(filialId, "Nome Update",
                    "Razão Update",
                    "52.130.577/0001-11",
                    "(45) 2012-3466",
                    "email2@email2.com",
                    SituacaoContrato.ATIVO,
                    enderecosDto);

            given(filialService.updateFilial(ArgumentMatchers.eq(filialId), ArgumentMatchers.eq(requestDtoUpdate))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(put("/filiais/update/{id}", filialId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDtoUpdate)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDtoUpdate)));

        }
    }

    @Nested
    class deleteFilialTest {

        @Test
        @DisplayName("Deve deletar a filial ao passar o id e retornar ResponseEntity.oK")
        void deleteFilialTest_V1() throws Exception {
            responseDto = new FilialResponseDto(filial);

            given(filialService.deleteFilial(ArgumentMatchers.eq(filialId))).willReturn(responseDto);
            ResultActions response = mockMvc.perform(delete("/filiais/delete/{id}", filialId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));

        }
    }

    @Nested
    class getFilialByIdTest {

        @Test
        @DisplayName("Deve encontrar uma filial pelo seu id e retornar um ResponseEntity.ok")
        void getFilialByIdTest_V1() throws Exception {
            var responseDto = new FilialResponseDto(filial);

            given(filialService.getFilialById(ArgumentMatchers.eq(filialId))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(get("/filiais/get/{id}", filialId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        }
    }

    @Nested
    class getFilialFilterTest {

        @Test
        @DisplayName("Deve retornar uma lista de FilialResponse pelo nome")
        void getFilialFilterTest_V1() throws Exception {
            var responseDtoList = List.of(
                    new FilialResponseDto(filial)

            );

            given(filialService.getFilialFilter(ArgumentMatchers.eq("Nome"))).willReturn(responseDtoList);

            ResultActions response = mockMvc.perform(get("/filiais/getAllFilter")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("nome", "Nome"));

            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.size()").value(responseDtoList.size()))
                    .andExpect(jsonPath("$[0].nomeFantasia").value("Nome"));

        }
    }

    @Nested
    class getFilialPagedTest {

        @Test
        @DisplayName("Deve retornar uma lista paginada com filtro")
        void getFilialPagedTest_V1() throws Exception {
            Pageable pageable = PageRequest.of(0, 1);
            Page<FilialResponseDto> responseDtoList = new PageImpl<>(List.of(
                    new FilialResponseDto(filial)
            ));
            var pagedRequest = new FilialPagedRequestDto(
                    null,
                    null,
                    null,
                    null,
                    null,
                    SituacaoContrato.ATIVO,
                    null,
                    null,
                    null);

            given(filialService.getFilialPaged(ArgumentMatchers.eq(pagedRequest), ArgumentMatchers.eq(pageable))).willReturn(responseDtoList);

            ResultActions response = mockMvc.perform(put("/filiais/getAllPaged")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("page", "0")
                    .param("size", "1")
                    .content(objectMapper.writeValueAsString(pagedRequest)));

            response.andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].situacaoContrato").value("ATIVO"))
                    .andExpect(jsonPath("$.totalElements").value(responseDtoList.getTotalElements()))
                    .andExpect(jsonPath("$.totalPages").value(responseDtoList.getTotalPages()))
                    .andExpect(jsonPath("$.size").value(pageable.getPageSize()))
                    .andExpect(jsonPath("$.number").value(pageable.getPageNumber()));
        }
    }

    @Nested
    class getFilialChartTest {

        @Test
        @DisplayName("Deve retornar uma lista com a quantidade de fornecedores, grupo de produtos, produtos, produto com maior valor e preço de venda")
        void getFilialChartTest_V1() throws Exception {
            var fornecedor = new Fornecedor();
            var grupoProduto = new GrupoProduto();
            var produto = createProduto(1L, "Produto 1", 1L, BigDecimal.TEN, BigDecimal.ONE, grupoProduto);
            var produto2 = createProduto(2L, "Produto 2", 1L, BigDecimal.TEN, new BigDecimal("50"), grupoProduto);

            filial.setProdutos(List.of(produto, produto2));
            filial.setGrupoProdutos(List.of(grupoProduto));
            filial.setFornecedores(List.of(fornecedor));

            var filiais = List.of(filial);
            var chartDto = filiais.stream().map(FilialChartDto::new).toList();

            given(filialService.getChart()).willReturn(chartDto);

            ResultActions response = mockMvc.perform(get("/filiais/getChart"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(chartDto.size()))
                    .andExpect(jsonPath("$[0].produtos").value(filial.getProdutos().size()))
                    .andExpect(jsonPath("$[0].gruposProduto").value(filial.getGrupoProdutos().size()))
                    .andExpect(jsonPath("$[0].fornecedores").value(filial.getFornecedores().size()))
                    .andExpect(jsonPath("$[0].maiorValorVenda.valorVenda").value(new BigDecimal("55.56")))
                    .andExpect(jsonPath("$[0].maiorValorProduto.valorProduto").value(new BigDecimal("50")));
        }
    }

    private Produto createProduto(Long id, String nome, Long grupoProdutoId, BigDecimal margem, BigDecimal valorProduto, GrupoProduto grupoProduto) {
        Produto produto = new Produto(
                new ProdutoRequestDto(
                        null,
                        nome, null,
                        grupoProdutoId,
                        null,
                        null,
                        margem,
                        true,
                        valorProduto,
                        SituacaoCadastro.ATIVO),
                grupoProduto);
        produto.setId(id);
        produto.calculateValorVenda();

        return produto;
    }
}