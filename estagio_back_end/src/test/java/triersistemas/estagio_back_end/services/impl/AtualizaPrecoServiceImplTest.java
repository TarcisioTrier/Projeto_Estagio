package triersistemas.estagio_back_end.services.impl;

import org.junit.jupiter.api.BeforeEach;
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
import triersistemas.estagio_back_end.enuns.AtualizaPrecoEnum;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.exceptions.InvalidMargemException;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepository;
import triersistemas.estagio_back_end.repository.ProdutoRepository;

import java.math.BigDecimal;
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

    @BeforeEach
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
    class atualizaPrecoMargemTest {

        @Test
        @DisplayName("Deve retornar uma lista de todos os produtos com a margem de lucro e o preço de venda atualizados (valor absoluto)")
        void atualizaPrecoMargemTest_V1() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, false, BigDecimal.TEN, false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            var expectedValuesVenda = expectedValue("11.11", "55.56");
            var expectedValuesMargem = expectedValue("10", "10");

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(expectedValuesVenda, actual.stream().map(ProdutoResponseDto::valorVenda).toList());
            assertEquals(expectedValuesMargem, actual.stream().map(ProdutoResponseDto::margemLucro).toList());


            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }

        @Test
        @DisplayName("Deve retornar uma lista de todos os produtos com a margem de lucro e o preço de venda atualizados (valor relativo)")
        void atualizaPrecoMargemTest_V2() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, true, BigDecimal.TEN, false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            var expectedValuesVenda = expectedValue("11.24", "56.18");
            var expectedValuesMargem = expectedValue("11", "11");

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();


            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(expectedValuesVenda, actual.stream().map(ProdutoResponseDto::valorVenda).toList());
            assertEquals(expectedValuesMargem, actual.stream().map(ProdutoResponseDto::margemLucro).toList());


            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }

        @Test
        @DisplayName("Deve retornar com apenas o produto selecionado com a margem de lucro e o preço de venda atualizados")
        void atualizaPrecoMargemTest_V3() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(List.of(1L), null, false, true, false, BigDecimal.TEN, false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto, produto2);

            var expectedValuesVenda = expectedValue("11.11", "50.51");
            var expectedValuesMargem = expectedValue("10", "1");

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected.getFirst(), actual.getFirst());
            assertEquals(expectedValuesVenda, actual.stream().map(ProdutoResponseDto::valorVenda).toList());
            assertEquals(expectedValuesMargem, actual.stream().map(ProdutoResponseDto::margemLucro).toList());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository).saveAll(produtoArgumentCaptor.capture());
        }

        @Test
        @DisplayName("Deve retornar uma lista de produtos que foram atualizados pela margem atualizada de todos grupos de produtos (valor relativo)")
        void atualizaPrecoMargemTest_V4() {
            Long filialId = 1L;
            setupAtualizaPrecoDto(null, null, true, false, true, BigDecimal.TEN, false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(1L);
            var grupoProduto2 = createGrupoProduto(2L);
            var produto1 = createProduto(1L, "produto 1", 1L, null, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", 2L, BigDecimal.ONE, new BigDecimal("50"), grupoProduto2);
            List<Produto> produtosList = List.of(produto1, produto2);

            grupoProduto.setProdutos(List.of(produto1));
            grupoProduto2.setProdutos(List.of(produto2));

            List<GrupoProduto> grupoProdutosList = List.of(grupoProduto, grupoProduto2);

            var expectedValuesVenda = expectedValue("11.24", "50.51");
            var expectedValuesMargemGrupo = expectedValue("11", "11");

            doReturn(grupoProdutosList).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue();

            assertNotNull(actual);
            assertEquals(expected.stream().map(ProdutoResponseDto::new).toList(), actual);
            assertEquals(expectedValuesVenda, actual.stream().map(ProdutoResponseDto::valorVenda).toList());
            assertEquals(expectedValuesMargemGrupo, expected.stream().map(c -> c.getGrupoProduto().getMargemLucro()).toList());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }

        @Test
        @DisplayName("Deve retornar uma lista de produtos que foram atualizados pela margem atualizada dos todos grupos de produtos (valor absoluto)")
        void atualizaPrecoMargemTest_V5() {
            Long filialId = 1L;
            setupAtualizaPrecoDto(null, null, true, false, false, BigDecimal.TEN, false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(1L);
            var grupoProduto2 = createGrupoProduto(2L);
            var produto1 = createProduto(1L, "produto 1", 1L, null, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", 2L, BigDecimal.ONE, new BigDecimal("50"), grupoProduto2);
            grupoProduto.setProdutos(List.of(produto1));
            grupoProduto2.setProdutos(List.of(produto2));

            List<GrupoProduto> grupoProdutosList = List.of(grupoProduto, grupoProduto2);

            List<Produto> produtosList = List.of(produto1, produto2);



            var expectedValuesVenda = expectedValue("11.11", "50.51");
            var expectedValuesMargemGrupo = expectedValue("10", "10");

            doReturn(grupoProdutosList).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue();

            assertNotNull(actual);
            assertEquals(expected.stream().map(ProdutoResponseDto::new).toList(), actual);
            assertEquals(expectedValuesVenda, actual.stream().map(ProdutoResponseDto::valorVenda).toList());
            assertEquals(expectedValuesMargemGrupo, expected.stream().map(c -> c.getGrupoProduto().getMargemLucro()).toList());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }

        @Test
        @DisplayName("Deve retornar uma lista de produtos que foram atualizados pela margem atualizada do grupo de produtos selecionado")
        void atualizaPrecoMargemTest_V6() {
            Long filialId = 1L;
            setupAtualizaPrecoDto(null, List.of(1L), false, false, false, BigDecimal.TEN, false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(1L);
            var grupoProduto2 = createGrupoProduto(2L);
            var produto1 = createProduto(1L, "produto 1", 1L, null, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", 2L, BigDecimal.ONE, new BigDecimal("50"), grupoProduto2);
            List<Produto> produtosList = List.of(produto1, produto2);

            grupoProduto.setProdutos(List.of(produto1));
            grupoProduto2.setProdutos(List.of(produto2));

            List<GrupoProduto> grupoProdutosList = List.of(grupoProduto, grupoProduto2);

            var expectedValuesVenda = new BigDecimal("11.11");
            var expectedValuesMargemGrupo = BigDecimal.TEN;
            List<Produto> produtosListExpected = List.of(produto1);

            doReturn(grupoProdutosList).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosListExpected).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue();

            assertNotNull(actual);
            assertEquals(expected.stream().map(ProdutoResponseDto::new).toList(), actual);
            assertEquals(expectedValuesVenda, actual.getFirst().valorVenda());
            assertEquals(expectedValuesMargemGrupo, expected.getFirst().getGrupoProduto().getMargemLucro());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosListExpected);
        }
    }

    @Nested
    class atualizaPrecoProdutoTest {

        @Test
        @DisplayName("Deve retornar uma lista de todos os produtos com o valor dos produtos e o preço de venda atualizados ao alterar o valor do produto (valor relativo)")
        void atualizaPrecoProdutoTest_V1() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, true, BigDecimal.TEN, false, AtualizaPrecoEnum.VALOR_PRODUTO);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            var expectedValuesProduto = expectedValue("20", "60");
            var expectedValuesVenda = expectedValue("20.20", "60.61");

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(expectedValuesProduto, actual.stream().map(ProdutoResponseDto::valorProduto).toList());
            assertEquals(expectedValuesVenda, actual.stream().map(ProdutoResponseDto::valorVenda).toList());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }

        @Test
        @DisplayName("Deve retornar uma lista de produtos com o valor dos produtos e o preço de venda atualizados ao alterar o valor do produto (valor absoluto)")
        void atualizaPrecoProdutoTest_V2() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, false, BigDecimal.TEN, false, AtualizaPrecoEnum.VALOR_PRODUTO);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            produto2.setAtualizaPreco(false);
            List<Produto> produtosList = List.of(produto1, produto2);

            var expectedValuesProduto = new BigDecimal("10");
            var expectedValuesVenda = new BigDecimal("10.10");
            List<Produto> produtosListExpected = List.of(produto1);

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosListExpected).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(expectedValuesProduto, actual.getFirst().valorProduto());
            assertEquals(expectedValuesVenda, actual.getFirst().valorVenda());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosListExpected);
        }

        @Test
        @DisplayName("Deve retornar um produto selecionado com o valor dos produtos e o preço de venda atualizados ao alterar o valor do produto (valor relativo e percentual)")
        void atualizaPrecoProdutoTest_V3() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(List.of(1L), null, false, true, true, BigDecimal.TEN, true, AtualizaPrecoEnum.VALOR_PRODUTO);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            var expectedValuesProduto = new BigDecimal("11.00");
            var expectedValuesVenda = new BigDecimal("11.11");
            List<Produto> produtosListExpected = List.of(produto1);

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosListExpected).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(expectedValuesProduto, actual.getFirst().valorProduto());
            assertEquals(expectedValuesVenda, actual.getFirst().valorVenda());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosListExpected);
        }

        @Test
        @DisplayName("Deve retornar um produto selecionado com o valor dos produtos e o preço de venda atualizados ao alterar o valor do produto (valor absoluto e percentual)")
        void atualizaPrecoProdutoTest_V4() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(List.of(1L), null, false, true, false, BigDecimal.TEN, true, AtualizaPrecoEnum.VALOR_PRODUTO);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            var expectedValuesProduto = new BigDecimal("1.00");
            var expectedValuesVenda = new BigDecimal("1.01");
            List<Produto> produtosListExpected = List.of(produto1);

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosListExpected).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(expectedValuesProduto, actual.getFirst().valorProduto());
            assertEquals(expectedValuesVenda, actual.getFirst().valorVenda());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosListExpected);
        }

        @Test
        @DisplayName("Deve retornar uma lista de produtos com o valor da margen e preço de venda atualizados ao alterar o valor da venda (valor relativo)")
        void atualizaPrecoProdutoTest_V5() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, true, BigDecimal.TEN, false, AtualizaPrecoEnum.VALOR_VENDA);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            var expectedValuesMargem = expectedValue("50.00", "17.00");
            var expectedValuesVenda = expectedValue("20.10", "60.51");

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(expectedValuesMargem, actual.stream().map(ProdutoResponseDto::margemLucro).toList());
            assertEquals(expectedValuesVenda, actual.stream().map(ProdutoResponseDto::valorVenda).toList());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }

        @Test
        @DisplayName("Deve retornar uma lista de produtos com o valor da margen e preço de venda atualizados ao alterar o valor da venda (valor absoluto)")
        void atualizaPrecoProdutoTest_V6() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, false, new BigDecimal("50"), false, AtualizaPrecoEnum.VALOR_VENDA);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            var expectedValuesMargem = expectedValue("80.00", "0.00");
            var expectedValuesVenda = expectedValue("50", "50");

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(expectedValuesMargem, actual.stream().map(ProdutoResponseDto::margemLucro).toList());
            assertEquals(expectedValuesVenda, actual.stream().map(ProdutoResponseDto::valorVenda).toList());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }

        @Test
        @DisplayName("Deve retornar uma lista de produtos com o valor da margen e preço de venda atualizados ao alterar o valor da venda (valor relativo e percentual)")
        void atualizaPrecoProdutoTest_V7() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, true, new BigDecimal("100.00"), true, AtualizaPrecoEnum.VALOR_VENDA);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            var expectedValuesMargem = expectedValue("50.00", "51.00");
            var expectedValuesVenda = expectedValue("20.2000", "101.0200");

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(expectedValuesMargem, actual.stream().map(ProdutoResponseDto::margemLucro).toList());
            assertEquals(expectedValuesVenda, actual.stream().map(ProdutoResponseDto::valorVenda).toList());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }

        @Test
        @DisplayName("Deve retornar uma lista de produtos com o valor da margen e preço de venda atualizados ao alterar o valor da venda (valor absoluto e percentual)")
        void atualizaPrecoProdutoTest_V8() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, false, new BigDecimal("95.00"), true, AtualizaPrecoEnum.VALOR_VENDA);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.TEN, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.TEN, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            var expectedValuesMargem = expectedValue("5.00", "5.00");
            var expectedValuesVenda = expectedValue("10.5545", "52.7820");

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).saveAll(produtoArgumentCaptor.capture());

            var actual = atualizaPrecoService.atualizaPreco(atualizaPrecoDto);

            var expected = produtoArgumentCaptor.getValue().stream().map(ProdutoResponseDto::new).toList();

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(expectedValuesMargem, actual.stream().map(ProdutoResponseDto::margemLucro).toList());
            assertEquals(expectedValuesVenda, actual.stream().map(ProdutoResponseDto::valorVenda).toList());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
            verify(produtoRepository, times(1)).saveAll(produtosList);
        }
    }

    @Nested
    class atualizaPrecoExceptionTest {

        @Test
        @DisplayName("testeValorTest - Deve lançar InvalidMargemException quando o valor do produto for negativo")
        void atualizaPrecoExceptionTest_V1() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, false, BigDecimal.valueOf(-1.00), false, AtualizaPrecoEnum.VALOR_PRODUTO);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);

            InvalidMargemException exception = assertThrows(InvalidMargemException.class,
                    () -> atualizaPrecoService.atualizaPreco(atualizaPrecoDto));
            assertEquals("Valor não pode ser negativo", exception.getMessage());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
        }
        @Test
        @DisplayName("testeValorMargemTest - Deve lançar InvalidMargemException quando a margem for negativo")
        void atualizaPrecoExceptionTest_V2() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, false, BigDecimal.valueOf(-1.00), false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);

            InvalidMargemException exception = assertThrows(InvalidMargemException.class,
                    () -> atualizaPrecoService.atualizaPreco(atualizaPrecoDto));
            assertEquals("Valor da Margem não pode ser negativo", exception.getMessage());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
        }

        @Test
        @DisplayName("testeValorMargemTest - Deve lançar InvalidMargemException quando a margem for maior que 100%")
        void atualizaPrecoExceptionTest_V3() {
            Long filialId = 1L;
            Long grupoProdutoId = 1L;
            setupAtualizaPrecoDto(null, null, true, true, false, BigDecimal.valueOf(120.00), false, AtualizaPrecoEnum.MARGEM);

            var grupoProduto = createGrupoProduto(grupoProdutoId);
            var produto1 = createProduto(1L, "produto 1", grupoProdutoId, BigDecimal.ONE, BigDecimal.TEN, grupoProduto);
            var produto2 = createProduto(2L, "produto 2", grupoProdutoId, BigDecimal.ONE, new BigDecimal("50"), grupoProduto);
            List<Produto> produtosList = List.of(produto1, produto2);

            doReturn(List.of(grupoProduto)).when(grupoProdutoRepository).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            doReturn(produtosList).when(produtoRepository).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);

            InvalidMargemException exception = assertThrows(InvalidMargemException.class,
                    () -> atualizaPrecoService.atualizaPreco(atualizaPrecoDto));
            assertEquals("Valor da Margem não pode ser maior que ou igual a 100%", exception.getMessage());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(atualizaPrecoDto.grupoProdutoFilter(), filialId);
            verify(produtoRepository, times(1)).buscarProduto(atualizaPrecoDto.produtoFilter(), filialId);
        }

    }

    private void setupAtualizaPrecoDto(List<Long> produtoId, List<Long> grupoProdutoId, boolean all, boolean isProduto, boolean isRelative, BigDecimal valor, boolean isPercentual, AtualizaPrecoEnum atualizaPrecoEnum) {
        atualizaPrecoDto = new AtualizaPrecoDto(
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

    private List<BigDecimal> expectedValue(String valorP1, String valorP2) {
        return List.of(new BigDecimal(valorP1), new BigDecimal(valorP2));
    }

}