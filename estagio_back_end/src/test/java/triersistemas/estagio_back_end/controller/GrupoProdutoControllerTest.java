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
import triersistemas.estagio_back_end.dto.request.GrupoProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoChartDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;
import triersistemas.estagio_back_end.services.GrupoProdutoService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = GrupoProdutoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class GrupoProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GrupoProdutoService grupoProdutoService;

    @Autowired
    private ObjectMapper objectMapper;

    private GrupoProduto grupoProduto;

    private Filial filial;

    private GrupoProdutoRequestDto requestDto;

    private GrupoProdutoResponseDto responseDto;

    private final Long grupoProdutoId = 1L;


    @BeforeEach
    void setUp() {
        filial = new Filial();
        filial.setId(1L);

        requestDto = new GrupoProdutoRequestDto(
                "Nome",
                TipoGrupoProduto.BEBIDA,
                BigDecimal.TEN,
                true,
                SituacaoCadastro.ATIVO,
                filial.getId());

        grupoProduto = new GrupoProduto(requestDto, filial);
    }

    @Nested
    class addGrupoProdutoTest {

        @Test
        @DisplayName("Deve adicionar um grupo de produtos e retornar um ResponseEntity.ok")
        void addGrupoProdutoTest_V1() throws Exception {

            responseDto = new GrupoProdutoResponseDto(grupoProduto);

            given(grupoProdutoService.addGrupoProduto(ArgumentMatchers.eq(requestDto))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(post("/grupos-produtos/post")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        }
    }

    @Nested
    class updateGrupoProdutoTest {

        @Test
        @DisplayName("Deve atualizar um grupo de produtos e retornar um ResponseEntity.ok")
        void updateGrupoProdutoTest_V1() throws Exception {

            var requestDtoUpdate = new GrupoProdutoRequestDto(
                    "Nome Update",
                    TipoGrupoProduto.BEBIDA,
                    BigDecimal.ONE,
                    true,
                    SituacaoCadastro.ATIVO,
                    filial.getId());

            responseDto = new GrupoProdutoResponseDto(
                    grupoProdutoId,
                    "Nome Update",
                    TipoGrupoProduto.BEBIDA,
                    BigDecimal.ONE,
                    true,
                    SituacaoCadastro.ATIVO,
                    filial.getId());

            given(grupoProdutoService.updateGrupoProduto(eq(grupoProdutoId), eq(requestDtoUpdate))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(put("/grupos-produtos/put/{id}", grupoProdutoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDtoUpdate)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDtoUpdate)))
                    .andExpect(jsonPath("$.nomeGrupo").value("Nome Update"))
                    .andExpect(jsonPath("$.margemLucro").value(BigDecimal.ONE));
        }
    }

    @Nested
    class deleteGrupoProdutoTest {

        @Test
        @DisplayName("Deve deletar um grupo de produtos pelo id e retornar um ResponseEntity.ok")
        void deleteGrupoProdutoTest_V1() throws Exception {

            responseDto = new GrupoProdutoResponseDto(grupoProduto);

            given(grupoProdutoService.deleteGrupoProduto(eq(grupoProdutoId))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(delete("/grupos-produtos/delete/{id}", grupoProdutoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk());
            response.andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        }
    }

    @Nested
    class getGrupoProdutoByIdTest {

        @Test
        @DisplayName("Deve encontrar um grupo de produtos pelo id e retornar um ResponseEntity.ok")
        void getGrupoProdutoByIdTest_V1() throws Exception {

            responseDto = new GrupoProdutoResponseDto(grupoProduto);

            given(grupoProdutoService.getGrupoProdutoById(eq(grupoProdutoId))).willReturn(responseDto);

            ResultActions response = mockMvc.perform(get("/grupos-produtos/get/{id}", grupoProdutoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)));

            response.andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        }
    }

    @Nested
    class getGrupoProdutoFilterTest {

        @Test
        @DisplayName("Deve encontrar uma lista de grupo de produtos pelo nome")
        void getGrupoProdutoFilterTest_V1() throws Exception {
            var responseDtoList = List.of(
                    new GrupoProdutoResponseDto(grupoProduto)
            );

            given(grupoProdutoService.getGrupoProdutoFilter(eq("Nome"), eq(filial.getId()))).willReturn(responseDtoList);

            ResultActions response = mockMvc.perform(get("/grupos-produtos/getAllFilter")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("nomeGrupo", "Nome")
                    .param("filialId", String.valueOf(filial.getId())));


            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.size()").value(responseDtoList.size()))
                    .andExpect(jsonPath("$[0].nomeGrupo").value("Nome"));
        }
    }

    @Nested
    class getGrupoProdutoPagedTest {

        @Test
        @DisplayName("Deve retornar uma lista paginada de grupo de produtos com filtro")
        void getGrupoProdutoPagedTest_V1() throws Exception {
            Pageable pageable = PageRequest.of(0, 1);

            Page<GrupoProdutoResponseDto> responseDtoList = new PageImpl<>(List.of(
                    new GrupoProdutoResponseDto(grupoProduto)
            ));

            var pagedRequest = new GrupoProdutoPagedRequestDto(
                    null,
                    null,
                    null,
                    true,
                    SituacaoCadastro.ATIVO,
                    null,
                    null
            );
            given(grupoProdutoService.getGrupoProdutoPaged(eq(pagedRequest), eq(filial.getId()), eq(pageable))).willReturn(responseDtoList);

            ResultActions response = mockMvc.perform(put("/grupos-produtos/getAllPaged")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("page", "0")
                    .param("size", "1")
                    .param("filialId", String.valueOf(filial.getId()))
                    .content(objectMapper.writeValueAsString(pagedRequest)));

            response.andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].situacaoCadastro").value("ATIVO"))
                    .andExpect(jsonPath("$.totalElements").value(responseDtoList.getTotalElements()))
                    .andExpect(jsonPath("$.totalPages").value(responseDtoList.getTotalPages()))
                    .andExpect(jsonPath("$.size").value(pageable.getPageSize()))
                    .andExpect(jsonPath("$.number").value(pageable.getPageNumber()));
        }
    }

    @Nested
    class  getProdutosTest{

        @Test
        @DisplayName("Deve retornar uma lista com a quantidade de produtos cadastrados dentro de um grupo de produtos")
        void getProdutosTest_V1() throws Exception{

            var produto = createProduto(1L, "Produto 1", 1L, BigDecimal.TEN, BigDecimal.ONE, grupoProduto);
            var produto2 = createProduto(2L, "Produto 2", 1L, BigDecimal.TEN, new BigDecimal("50"), grupoProduto);

            grupoProduto.setProdutos(List.of(produto,produto2));

            var gruposProdutos = List.of(grupoProduto);
            filial.setGrupoProdutos(gruposProdutos);

            var chartDto = gruposProdutos.stream().map(GrupoProdutoChartDto::new).toList();

            given(grupoProdutoService.getProdutos(eq(filial.getId()))).willReturn(chartDto);

            ResultActions response = mockMvc.perform(get("/grupos-produtos/getProdutos/{id}", filial.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(chartDto.size()))
                    .andExpect(jsonPath("$[0].nomeGrupo").value("Nome"))
                    .andExpect(jsonPath("$[0].produtos").value(grupoProduto.getProdutos().size()));
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