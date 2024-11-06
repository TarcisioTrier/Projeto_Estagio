package triersistemas.estagio_back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.N;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.services.ProdutoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProdutoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Produto produto;

    private ProdutoResponseDto responseDto;

    private ProdutoRequestDto requestDto;

    private final Long produtoId = 1L;

    private GrupoProduto grupoProduto;

    @BeforeEach
    void setUp() {
        grupoProduto = new GrupoProduto();
        grupoProduto.setId(1L);

        requestDto = new ProdutoRequestDto(
                "3123122313121",
                "Nome",
                "Descrição",
                grupoProduto.getId(),
                TipoProduto.GENERICO,
                Apresentacao.INJETAVEL,
                BigDecimal.TEN,
                true,
                BigDecimal.TEN,
                SituacaoCadastro.ATIVO);

        produto = new Produto(requestDto, grupoProduto);
        produto.calculateValorVenda();
    }

    @Nested
    class postProdutoTest {

        @Test
        @DisplayName("Deve cadastrar um produto e retornar um ResponseEntity.ok")
        void postProdutoTest_V1() throws Exception {
            responseDto = new ProdutoResponseDto(produto);

            given(produtoService.addProduto(eq(requestDto))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(post("/produto/post")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        }
    }

    @Nested
    class updateProdutoTest {

        @Test
        @DisplayName("Deve atualizar um produto e retonar um ResponseEntity.ok")
        void updateProdutoTest_V1() throws Exception {

            var requestDtoUpdate = new ProdutoRequestDto(
                    "5901234123457",
                    "Nome Update",
                    "Descrição Update",
                    grupoProduto.getId(),
                    TipoProduto.GENERICO,
                    Apresentacao.INJETAVEL,
                    BigDecimal.TEN,
                    true,
                    BigDecimal.TEN,
                    SituacaoCadastro.ATIVO);

            responseDto = new ProdutoResponseDto(
                    produtoId,
                    "5901234123457",
                    "Nome Update",
                    "Descrição Update",
                    grupoProduto.getId(),
                    TipoProduto.GENERICO,
                    Apresentacao.INJETAVEL,
                    BigDecimal.TEN,
                    true,
                    BigDecimal.TEN,
                    new BigDecimal("11.11"),
                    LocalDate.now(),
                    SituacaoCadastro.ATIVO);

            given(produtoService.updateProduto(eq(produtoId), eq(requestDtoUpdate))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(put("/produto/put/{id}", produtoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDtoUpdate)));


            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Nome Update"))
                    .andExpect(jsonPath("$.descricao").value("Descrição Update"))
                    .andExpect(jsonPath("$.codigoBarras").value("5901234123457"))
                    .andExpect(jsonPath("$.valorVenda").value(produto.getValorVenda()));
        }
    }

    @Nested
    class deleteProdutoTest {

        @Test
        @DisplayName("Deve deletar um produto e retornar um ResponseEntity.ok")
        void deleteProdutoTest_V1() throws Exception {

            responseDto = new ProdutoResponseDto(produto);

            given(produtoService.deleteProduto(eq(produtoId))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(delete("/produto/delete/{id}", produtoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        }
    }

    @Nested
    class getProdutoByIdTest {

        @Test
        @DisplayName("Deve encontrar um produto e retornar um ResponseEntity.ok")
        void getProdutoByIdTest_V1() throws Exception {

            responseDto = new ProdutoResponseDto(produto);

            given(produtoService.getProdutoById(eq(produtoId))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(get("/produto/get/{id}", produtoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        }
    }

    @Nested
    class getProdutoPagedTest {

        @Test
        @DisplayName("Deve retornar uma lista paginada de produtos com filtro")
        void getProdutoPagedTest_V1() throws Exception {
            Pageable pageable = PageRequest.of(0, 1);
            var filialId = 1L;
            var responseDtoList = new PageImpl<>(List.of(
                    new ProdutoResponseDto(produto)
            ));

            var pagedRequest = new ProdutoPagedRequestDto(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    true,
                    null,
                    null,
                    null,
                    SituacaoCadastro.ATIVO,
                    null,
                    null);

            given(produtoService.getProdutoPaged(eq(pagedRequest), eq(filialId), eq(pageable))).willReturn(responseDtoList);

            ResultActions response = mockMvc.perform(put("/produto/getAllPaged")
                    .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pagedRequest))
                    .param("filialId", String.valueOf(filialId))
                    .param("page", "0")
                    .param("size", "1"));

            response.andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].situacaoCadastro").value("ATIVO"))
                    .andExpect(jsonPath("$.content[0].atualizaPreco").value(true))
                    .andExpect(jsonPath("$.totalElements").value(responseDtoList.getTotalElements()))
                    .andExpect(jsonPath("$.totalPages").value(responseDtoList.getTotalPages()))
                    .andExpect(jsonPath("$.size").value(pageable.getPageSize()))
                    .andExpect(jsonPath("$.number").value(pageable.getPageNumber()));
        }
    }
}