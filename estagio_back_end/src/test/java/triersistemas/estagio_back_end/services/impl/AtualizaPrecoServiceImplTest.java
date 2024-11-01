package triersistemas.estagio_back_end.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.AtualizaPrecoEnum;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepository;
import triersistemas.estagio_back_end.repository.ProdutoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizaPrecoServiceImplTest {

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    GrupoProdutoRepository grupoProdutoRepository;

    @InjectMocks
    AtualizaPrecoServiceImpl atualizaPrecoService;

    private AtualizaPrecoDto atualizaPrecoDto;
    private ProdutoPagedRequestDto produtoFilter;
    private GrupoProdutoPagedRequestDto grupoProdutoFilter;

    @Captor
    ArgumentCaptor<List<Produto>> produtoArgumentCaptor;

    void setUp() {
        produtoFilter = new ProdutoPagedRequestDto(
                null, null, null, null, null, null,
                null, true, null, null, null,
                SituacaoCadastro.ATIVO, null, null);

        grupoProdutoFilter = new GrupoProdutoPagedRequestDto(
                null, null, null, true,
                SituacaoCadastro.ATIVO, null, null);


    }

    @Nested
    class AtualizaPrecoTest {

        @Test
        @DisplayName("Deve retornar uma lista de todos os produtos com a margem de lucro e o preço de venda atualizados (valor absoluto)")
        void atualizaPrecoTest_V1() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(List.of(1L, 2L),null,true, true, false, false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtosList);

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);
            List<ProdutoResponseDto> expected = produtosList.stream().map(ProdutoResponseDto::new).toList();

            System.out.println(actual);
            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(2, actual.size());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }

        @Test
        @DisplayName("Deve retornar uma lista de todos os produtos com a margem de lucro e o preço de venda atualizados (valor relativo)")
        void atualizaPrecoTest_V2() {
            setupAtualizaPrecoDto(List.of(1L, 2L),null, true, true, true, false, AtualizaPrecoEnum.MARGEM);

            Long filialId = 1L;
            Long grupoProdutoId = 1L;

            var grupoProduto = createGrupoProduto(grupoProdutoId);

            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtosList);

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);
            List<ProdutoResponseDto> expected = produtosList.stream().map(ProdutoResponseDto::new).toList();

            System.out.println(actual);
            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(2, actual.size());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }

        @Test
        @DisplayName("Deve retornar uma lista com um produto selecionado com a margem de lucro e o preço de venda atualizados")
        void atualizaPrecoTest_V3() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(List.of(1L),null, false, true, false, false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(grupoProdutoId);

            var produto = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto, produto2);

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);
            List<ProdutoResponseDto> expected = produtosList.stream().map(ProdutoResponseDto::new).toList();


            var listCaptured = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();
            System.out.println(actual);

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(listCaptured.getFirst(), actual.getFirst());
            assertEquals(BigDecimal.ONE, actual.getLast().margemLucro());
            assertNotEquals(BigDecimal.ONE, actual.getFirst().margemLucro());
            assertEquals(1, listCaptured.size());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository).saveAll(produtoArgumentCaptor.capture());
        }

        @Test
        @DisplayName("Deve retornar uma lista de produtos que foram atualizados pela margem atualizada do grupo de produto")
        void atualizaPrecoTest_V4() {
            Long filialId = 1L;
            setupAtualizaPrecoDto(null,List.of(1L), false, false, true, false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(1L);
            var grupoProduto2 = createGrupoProduto(2L);
            List<GrupoProduto> grupoProdutosList = List.of(grupoProduto,grupoProduto2);

            var produto1 = createProduto(1L, "produto 1", 1L, null, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", 2L, BigDecimal.ONE, new BigDecimal("50"), grupoProduto2);
            List<Produto> produtosList = List.of(produto1, produto2);
            
            doReturn(grupoProdutosList).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);
            List<ProdutoResponseDto> expected = produtosList.stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected,actual);


            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }
    }

    private void setupAtualizaPrecoDto(List<Long> produtoId,List<Long> grupoProdutoId, boolean all, boolean isProduto, boolean isRelative, boolean isPercentual, AtualizaPrecoEnum atualizaPrecoEnum) {
        atualizaPrecoDto = new AtualizaPrecoDto(
                produtoId,
                grupoProdutoId,
                1L, all, isProduto, isRelative,
                BigDecimal.TEN, isPercentual,
                atualizaPrecoEnum,
                produtoFilter, grupoProdutoFilter
        );
    }

    private GrupoProduto createGrupoProduto(Long grupoProdutoId) {
        GrupoProduto grupoProduto = new GrupoProduto();
        grupoProduto.setId(grupoProdutoId);
        grupoProduto.setMargemLucro(BigDecimal.TEN);
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