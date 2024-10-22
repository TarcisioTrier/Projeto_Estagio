package triersistemas.estagio_back_end.services.impl;

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
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.ProdutoRepository;
import triersistemas.estagio_back_end.services.GrupoProdutoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private GrupoProdutoService grupoProdutoService;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @Captor
    private ArgumentCaptor<Produto> produtoArgumentCaptor;


    @Nested
    class addProduto {

        @Test
        @DisplayName("Should save produto with success")
        void shouldSaveProdutoWithSuccess() {
            // Arrange
            var grupoProduto = new GrupoProduto();
            grupoProduto.setId(1L);

            var input = new ProdutoRequestDto(
                    "123456789",
                    "Produto Test",
                    "Descrição do Produto",
                    grupoProduto.getId(),
                    TipoProduto.OFICINAL,
                    Apresentacao.GOTAS,
                    BigDecimal.valueOf(0.2),
                    true,
                    BigDecimal.TEN,
                    SituacaoCadastro.ATIVO
            );

            var produto = new Produto();
            produto.setId(1L);
            produto.setCodigoBarras(input.codigoBarras());
            produto.setNome(input.nome());
            produto.setDescricao(input.descricao());
            produto.setGrupoProduto(grupoProduto);
            produto.setTipoProduto(input.tipoProduto());
            produto.setApresentacao(input.apresentacao());
            produto.setMargemLucro(input.margemLucro());
            produto.setAtualizaPreco(input.atualizaPreco());
            produto.setValorProduto(input.valorProduto());
            produto.setSituacaoCadastro(input.situacaoCadastro());
            produto.setDataUltimaAtualizacaoPreco(LocalDate.now());

            when(grupoProdutoService.grupoProdutoById(grupoProduto.getId())).thenReturn(grupoProduto);
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

            // Act
            var result = produtoService.addProduto(input);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals(input.codigoBarras(), result.codigoBarras());
            assertEquals(input.nome(), result.nome());
            assertEquals(input.descricao(), result.descricao());
            assertEquals(input.grupoProdutoId(), result.grupoProdutoId());
            assertEquals(input.tipoProduto(), result.tipoProduto());
            assertEquals(input.apresentacao(), result.apresentacao());
            assertEquals(input.margemLucro(), result.margemLucro());
            assertEquals(input.atualizaPreco(), result.atualizaPreco());
            assertEquals(input.valorProduto(), result.valorProduto());
            assertEquals(input.situacaoCadastro(), result.situacaoCadastro());
            assertNotNull(result.dataUltimaAtualizacaoPreco());

            verify(grupoProdutoService, times(1)).grupoProdutoById(grupoProduto.getId());
            verify(produtoRepository, times(1)).save(any(Produto.class));
        }
    }

    @Nested
    class getProdutoById {

        @Test
        @DisplayName("Should get Produto with valid id")
        void shouldGetProdutoWithValidId() {
            // Arrange
            var grupoProduto = new GrupoProduto();
            grupoProduto.setId(1L);

            var produto = new Produto();
            produto.setId(1L);
            produto.setGrupoProduto(grupoProduto);

            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            // Act
            var result = produtoService.getProdutoById(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.id());
            verify(produtoRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw NotFoundException with invalid id")
        void shouldThrowNotFoundExceptionWithInvalidId() {
            // Arrange
            Long id = 1L;
            when(produtoRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> produtoService.getProdutoById(id));
            verify(produtoRepository, times(1)).findById(1L);
        }
    }

    @Nested
    class updateProduto {

        @Test
        @DisplayName("Should update Produto with valid id")
        void shouldUpdateProdutoWithValidId() {
            // Arrange
            var grupoProduto = new GrupoProduto();
            grupoProduto.setId(1L);

            var produto = new Produto();
            produto.setId(1L);
            produto.setGrupoProduto(grupoProduto);

            var inputDto = new ProdutoRequestDto(
                    "987654321",
                    "Produto Updated",
                    "Descrição Atualizada",
                    grupoProduto.getId(),
                    TipoProduto.OFICINAL,
                    Apresentacao.GOTAS,
                    BigDecimal.valueOf(0.25),
                    false,
                    BigDecimal.valueOf(15.0),
                    SituacaoCadastro.ATIVO
            );

            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(grupoProdutoService.grupoProdutoById(inputDto.grupoProdutoId())).thenReturn(grupoProduto);
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

            // Act
            var result = produtoService.updateProduto(1L, inputDto);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals(inputDto.codigoBarras(), result.codigoBarras());
            assertEquals(inputDto.nome(), result.nome());
            assertEquals(inputDto.descricao(), result.descricao());
            assertEquals(inputDto.grupoProdutoId(), result.grupoProdutoId());
            assertEquals(inputDto.tipoProduto(), result.tipoProduto());
            assertEquals(inputDto.apresentacao(), result.apresentacao());
            assertEquals(inputDto.margemLucro(), result.margemLucro());
            assertEquals(inputDto.atualizaPreco(), result.atualizaPreco());
            assertEquals(inputDto.valorProduto(), result.valorProduto());
            assertEquals(inputDto.situacaoCadastro(), result.situacaoCadastro());

            verify(produtoRepository, times(1)).findById(1L);
            verify(grupoProdutoService, times(1)).grupoProdutoById(inputDto.grupoProdutoId());
            verify(produtoRepository, times(1)).save(any(Produto.class));
        }
    }

    @Nested
    class deleteProdutoById {

        @Test
        @DisplayName("Should delete Produto with valid id")
        void shouldDeleteProdutoWithValidId() {

            // Arrange
            var grupoProduto = new GrupoProduto();
            grupoProduto.setId(1L);
            Long id = 1L;
            var produto = new Produto();
            produto.setId(id);
            produto.setGrupoProduto(grupoProduto);
            when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

            // Act
            var result = produtoService.deleteProduto(id);

            // Assert
            assertNotNull(result);
            assertEquals(id, result.id());
            verify(produtoRepository, times(1)).findById(id);
            verify(produtoRepository, times(1)).delete(produto);
        }
    }

    @Nested
    class getProdutoFilter {

        @Test
        @DisplayName("Should return filtered Produto page")
        void shouldReturnFilteredProdutoPage() {
            // Arrange
            String nome = "Test";
            TipoProduto tipo = TipoProduto.PERFUMARIA;
            Long grupoProdutoId = 1L;
            PageRequest pageable = PageRequest.of(0, 10);

            List<ProdutoResponseDto> produtoList = Arrays.asList(
                    new ProdutoResponseDto(
                            1L,
                            "123456789",
                            "Test Product 1",
                            "Description 1",
                            grupoProdutoId,
                            TipoProduto.PERFUMARIA,
                            Apresentacao.LIQUIDO,
                            BigDecimal.valueOf(0.2),
                            true,
                            BigDecimal.valueOf(10.0),
                            BigDecimal.valueOf(12.0),
                            LocalDate.now(),
                            SituacaoCadastro.ATIVO
                    ),
                    new ProdutoResponseDto(
                            2L,
                            "987654321",
                            "Test Product 2",
                            "Description 2",
                            grupoProdutoId,
                            TipoProduto.REFERENCIA,
                            Apresentacao.GOTAS,
                            BigDecimal.valueOf(0.25),
                            false,
                            BigDecimal.valueOf(20.0),
                            BigDecimal.valueOf(25.0),
                            LocalDate.now(),
                            SituacaoCadastro.ATIVO
                    )
            );
            Page<ProdutoResponseDto> expectedPage = new PageImpl<>(produtoList, pageable, produtoList.size());

            when(produtoRepository.buscarProduto(nome, tipo, grupoProdutoId, pageable)).thenReturn(expectedPage);

            // Act
            Page<ProdutoResponseDto> result = produtoService.getProdutoPaged(nome, tipo, grupoProdutoId, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
            assertEquals(expectedPage.getContent().size(), result.getContent().size());
            assertEquals(expectedPage.getNumber(), result.getNumber());
            assertEquals(expectedPage.getSize(), result.getSize());

            ProdutoResponseDto firstItem = result.getContent().getFirst();
            assertEquals(1L, firstItem.id());
            assertEquals("123456789", firstItem.codigoBarras());
            assertEquals("Test Product 1", firstItem.nome());
            assertEquals("Description 1", firstItem.descricao());
            assertEquals(grupoProdutoId, firstItem.grupoProdutoId());
            assertEquals(TipoProduto.PERFUMARIA, firstItem.tipoProduto());
            assertEquals(Apresentacao.LIQUIDO, firstItem.apresentacao());
            assertEquals(BigDecimal.valueOf(0.2), firstItem.margemLucro());
            assertTrue(firstItem.atualizaPreco());
            assertEquals(BigDecimal.valueOf(10.0), firstItem.valorProduto());
            assertEquals(BigDecimal.valueOf(12.0), firstItem.valorVenda());
            assertEquals(SituacaoCadastro.ATIVO, firstItem.situacaoCadastro());
            assertNotNull(firstItem.dataUltimaAtualizacaoPreco());

            verify(produtoRepository, times(1)).buscarProduto(nome, tipo, grupoProdutoId, pageable);
        }
    }

    @Nested
    class alteraProdutoById {

        @Test
        @DisplayName("Should change situaçãoCadastro to ativo status with valid id")
        void shouldChangeSituacaoCadastroToAtivoStatusWithValidId() {
            // Arrange
            var grupoProduto = new GrupoProduto();
            grupoProduto.setId(1L);
            Long id = 1L;
            var produto = new Produto();
            produto.setId(id);
            produto.setSituacaoCadastro(SituacaoCadastro.INATIVO);
            produto.setGrupoProduto(grupoProduto);

            when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

            // Act
            var result = produtoService.removeProduto(id);

            // Assert
            assertNotNull(result);
            assertEquals(SituacaoCadastro.INATIVO, result.situacaoCadastro());
            verify(produtoRepository, times(1)).findById(id);
            verify(produtoRepository, times(1)).save(produto);
        }

        @Test
        @DisplayName("Should change situaçãoCadastro to inativo status with valid id")
        void shouldChangeSituacaoCadastroToInativoStatusWithValidId() {

            // Arrange
            var grupoProduto = new GrupoProduto();
            grupoProduto.setId(1L);
            Long id = 1L;
            var produto = new Produto();
            produto.setId(id);
            produto.setSituacaoCadastro(SituacaoCadastro.ATIVO);
            produto.setGrupoProduto(grupoProduto);

            when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

            // Act
            var result = produtoService.removeProduto(id);

            // Assert
            assertNotNull(result);
            assertEquals(SituacaoCadastro.INATIVO, result.situacaoCadastro());
            verify(produtoRepository, times(1)).findById(id);
            verify(produtoRepository, times(1)).save(produto);
        }

        @Test
        @DisplayName("Should throw NotFoundException when produto not found")
        void shouldThrowNotFoundExceptionWhenProdutoNotFound() {
            // Arrange
            Long id = 1L;
            when(produtoRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> produtoService.removeProduto(id));
            verify(produtoRepository, times(1)).findById(id);
            verify(produtoRepository, never()).save(any(Produto.class));
        }
    }
}