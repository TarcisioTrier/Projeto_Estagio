package triersistemas.estagio_back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.AtualizaPrecoEnum;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.services.AtualizaPrecoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AtualizaPrecoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AtualizaPrecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtualizaPrecoService atualizaPrecoService;

    @Autowired
    private ObjectMapper objectMapper;

    private AtualizaPrecoDto atualizaPrecoDto;

    private ProdutoPagedRequestDto produtoFilter;

    private GrupoProdutoPagedRequestDto grupoProdutoFilter;

    private Filial filial;

    private final Long grupoProdutoId = 1L;

    @BeforeEach
    void setUp() {
        produtoFilter = new ProdutoPagedRequestDto(
                null, null, null, null, null, null,
                null, true, null, null, null,
                SituacaoCadastro.ATIVO, null, null);

        grupoProdutoFilter = new GrupoProdutoPagedRequestDto(
                null, null, null, true,
                SituacaoCadastro.ATIVO, null, null);

        filial =new Filial();
        filial.setId(1L);

        var grupoProduto = createGrupoProduto(grupoProdutoId);
        var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
        var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
        List<Produto> produtosList = List.of(produto1, produto2);
        filial.setGrupoProdutos(List.of(grupoProduto));
        filial.setProdutos(produtosList);
    }

    @Nested
    class atualizaPrecoTest{

        @Test
        @DisplayName("Deve atualizar a margem de todos os produtos e retornar um ResponseEntity.ok")
        void atualizaPrecoTest_V1() throws Exception {
            atualizaPrecoDto = setupAtualizaPrecoDto(null, null, true, true, false, BigDecimal.TEN, false, AtualizaPrecoEnum.MARGEM);

            var responseDto = List.of(
                    new ProdutoResponseDto(1L,null,"produto 1",null,grupoProdutoId,null,null,BigDecimal.TEN,true,BigDecimal.TEN,new BigDecimal("11.11"), LocalDate.now(),SituacaoCadastro.ATIVO),
                    new ProdutoResponseDto(2L,null,"produto 2",null,grupoProdutoId,null,null,BigDecimal.TEN,true,new BigDecimal("50"),new BigDecimal("55.56"), LocalDate.now(),SituacaoCadastro.ATIVO)
            );

            given(atualizaPrecoService.atualizaPreco(eq(atualizaPrecoDto))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(put("/atualiza/put")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(atualizaPrecoDto)));

            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].margemLucro").value(BigDecimal.TEN));
        }

    }

    private AtualizaPrecoDto setupAtualizaPrecoDto(List<Long> produtoId, List<Long> grupoProdutoId, boolean all, boolean isProduto, boolean isRelative, BigDecimal valor, boolean isPercentual, AtualizaPrecoEnum atualizaPrecoEnum) {
        return new AtualizaPrecoDto(
                produtoId,
                grupoProdutoId,
                1L, all, isProduto, isRelative,
                valor, isPercentual,
                atualizaPrecoEnum,
                produtoFilter, grupoProdutoFilter
        );
    }

    private GrupoProduto createGrupoProduto(Long grupoProdutoId) {
        GrupoProduto grupoProduto = new GrupoProduto();
        grupoProduto.setId(grupoProdutoId);
        grupoProduto.setMargemLucro(BigDecimal.ONE);
        grupoProduto.setSituacaoCadastro(SituacaoCadastro.ATIVO);
        grupoProduto.setAtualizaPreco(true);
        return grupoProduto;
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