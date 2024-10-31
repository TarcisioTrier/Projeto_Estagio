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
import triersistemas.estagio_back_end.dto.request.ProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.repository.ProdutoRepository;
import triersistemas.estagio_back_end.services.GrupoProdutoService;
import triersistemas.estagio_back_end.validators.BarcodeValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    GrupoProdutoService grupoProdutoService;

    @Mock
    BarcodeValidator barcodeValidator;

    @InjectMocks
    ProdutoServiceImpl produtoService;

    private ProdutoRequestDto requestDto;
    private final GrupoProduto grupoProduto = new GrupoProduto();
    private final Filial filial = new Filial();
    private Produto produto;
    private final Long grupoProdutoId = 1L;
    private final Long produtoId = 1L;
    private final Long filialId = 1L;

    @Captor
    ArgumentCaptor<Produto> produtoArgumentCaptor;

    @BeforeEach
    void setUp() {
        filial.setId(filialId);
        grupoProduto.setId(grupoProdutoId);
        grupoProduto.setFilial(filial);

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

        produto = new Produto(requestDto,grupoProduto);
    }

    @Nested
    class addProdutoTest {

        @Test
        @DisplayName("Deve retornar um produtoResponse e salva no banco, com os dados preenchidos")
        void addProdutoTest_V1() {

            doReturn(grupoProduto).when(grupoProdutoService).findById(grupoProdutoId);
            doNothing().when(barcodeValidator).validateBarcodePost(requestDto.codigoBarras(), grupoProduto.getFilial().getId());
            doAnswer(capture -> {
                Produto produtoCaptured = capture.getArgument(0);
                produtoCaptured.calculateValorVenda();
                return produtoCaptured;
            }).when(produtoRepository).save(produtoArgumentCaptor.capture());

            var actual = produtoService.addProduto(requestDto);
            var expected = new ProdutoResponseDto(produtoArgumentCaptor.getValue());

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(new BigDecimal("11.11"), actual.valorVenda());

            verify(grupoProdutoService, times(1)).findById(grupoProdutoId);
            verify(produtoRepository, times(1)).save(produtoArgumentCaptor.capture());
            verify(barcodeValidator, times(1)).validateBarcodePost(requestDto.codigoBarras(), grupoProduto.getFilial().getId());
        }
    }

    @Nested
    class updateProdutoTest {

        @Test
        @DisplayName("Deve retornar um produtoResponse e atualizar o banco de dados")
        void updateProdutoTest_V1() {

            var newRequestDto = new ProdutoRequestDto(
                    "3123122313121",
                    "Nome Update",
                    "Descrição Update",
                    grupoProduto.getId(),
                    TipoProduto.GENERICO,
                    Apresentacao.INJETAVEL,
                    BigDecimal.ONE,
                    true,
                    BigDecimal.TEN,
                    SituacaoCadastro.ATIVO);

            doReturn(Optional.of(grupoProduto)).when(grupoProdutoService).buscaGrupoProdutoPorId(grupoProdutoId);
            doReturn(Optional.of(produto)).when(produtoRepository).findById(produtoId);
            doNothing().when(barcodeValidator).validateBarcodeUpdate(newRequestDto.codigoBarras(), produtoId, filialId);
            doReturn(produto).when(produtoRepository).save(produtoArgumentCaptor.capture());

            var actual = produtoService.updateProduto(produtoId, newRequestDto);
            var expected = new ProdutoResponseDto(produtoArgumentCaptor.getValue());

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertEquals(new BigDecimal("10.10"), actual.valorVenda());

            verify(grupoProdutoService, times(1)).buscaGrupoProdutoPorId(grupoProdutoId);
            verify(produtoRepository, times(1)).findById(produtoId);
            verify(barcodeValidator).validateBarcodeUpdate(newRequestDto.codigoBarras(), produtoId, filialId);
            verify(produtoRepository, times(1)).save(produtoArgumentCaptor.capture());
        }
    }

    @Nested
    class deleteProdutoTest {

        @Test
        @DisplayName("Deve deletar e retornar um produtoResponse encontrado pelo id")
        void deleteProdutoTest_V1() {

            doReturn(Optional.of(produto)).when(produtoRepository).findById(produtoId);
            doNothing().when(produtoRepository).delete(produtoArgumentCaptor.capture());

            var actual = produtoService.deleteProduto(produtoId);
            var expected = new ProdutoResponseDto(produtoArgumentCaptor.getValue());

            assertEquals(expected,actual);

            verify(produtoRepository,times(1)).findById(produtoId);
            verify(produtoRepository,times(1)).delete(produtoArgumentCaptor.capture());
        }


    }

    @Nested
    class getProdutoByIdTest{

        @Test
        @DisplayName("Deve retornar um produto pelo id")
        void getProdutoByIdTest_V1() {

            doReturn(Optional.of(produto)).when(produtoRepository).findById(produtoId);

            var actual = produtoService.getProdutoById(produtoId);
            var expected = new ProdutoResponseDto(produto);

            assertNotNull(actual);
            assertEquals(expected,actual);

            verify(produtoRepository,times(1)).findById(produtoId);
        }
    }

    @Nested
    class getProdutoPagedTest{

        @Test
        @DisplayName("Deve retornar produtos que batem com o requestDto, usando Pageable")
        void getProdutoPagedTest_V1() {

            Pageable pageable = PageRequest.of(0, 5);
            var newRequestDto = new ProdutoPagedRequestDto(
                    produtoId,
                    null,
                    "Nome",
                    "Descrição",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            var responseDto = new ProdutoResponseDto(produto);
            Page<ProdutoResponseDto> expected = new PageImpl<>(List.of(responseDto));

            doReturn(new PageImpl<>(List.of(responseDto))).when(produtoRepository).buscarProduto(newRequestDto,filialId,pageable);

            Page<ProdutoResponseDto> actual = produtoService.getProdutoPaged(newRequestDto,filialId,pageable);

            assertEquals(expected.getTotalElements(), actual.getTotalElements());
            assertEquals(expected.getTotalPages(), actual.getTotalPages());
            assertEquals(expected.getContent().getFirst(), actual.getContent().getFirst());
        }
    }
}

