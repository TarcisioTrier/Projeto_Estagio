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
import triersistemas.estagio_back_end.dto.request.GrupoProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepository;
import triersistemas.estagio_back_end.services.FilialService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupoProdutoServiceImplTest {

    @Mock
    private GrupoProdutoRepository grupoProdutoRepository;

    @Mock
    private FilialService filialService;

    @InjectMocks
    private GrupoProdutoServiceImpl grupoProdutoService;

    @Captor
    private ArgumentCaptor<GrupoProduto> grupoProdutoArgumentCaptor;

    @Nested
    class addGrupoProduto {

        @Test
        @DisplayName("Should save grupoProduto with success")
        void shouldSaveGrupoProdutoWithSucess() {

            //Arrange
            var filial = new Filial();
            filial.setId(1L);

            var input = new GrupoProdutoRequestDto(
                    "GrupoProduto",
                    TipoGrupoProduto.OUTROS,
                    BigDecimal.TEN.multiply(new BigDecimal("0.3")),
                    true,
                    SituacaoCadastro.ATIVO,
                    filial.getId());
            var grupoProduto = new GrupoProduto(input, filial);
            grupoProduto.setId(1L);

            doReturn(filial).when(filialService).findById(filial.getId());
            doReturn(grupoProduto).when(grupoProdutoRepository).save(grupoProdutoArgumentCaptor.capture());

            //Act
            var result = grupoProdutoService.addGrupoProduto(input);

            //Assert
            assertNotNull(result);
            assertNotNull(result.id());

            var grupoProdutoCaptured = grupoProdutoArgumentCaptor.getValue();

            assertEquals(result.id(), 1L);
            assertEquals(input.nomeGrupo(), grupoProdutoCaptured.getNomeGrupo());
            assertEquals(input.tipoGrupo(), grupoProdutoCaptured.getTipoGrupo());
            assertEquals(input.margemLucro(), grupoProdutoCaptured.getMargemLucro());
            assertEquals(input.atualizaPreco(), grupoProdutoCaptured.getAtualizaPreco());
            assertEquals(input.situacaoCadastro(), grupoProdutoCaptured.getSituacaoCadastro());
            assertEquals(input.filialId(), grupoProdutoCaptured.getFilial().getId());

            verify(grupoProdutoRepository, times(1)).save(grupoProdutoCaptured);
            verify(filialService, times(1)).findById(filial.getId());
        }
    }

    @Nested
    class getGrupoProdutoById {

        @Test
        @DisplayName("Should get Grupo Produto with valid id")
        void shouldGetGrupoProdutoWithValidId() {
            //Arrange
            var filial = new Filial();
            filial.setId(1L);

            var input = new GrupoProdutoRequestDto(
                    "",
                    null,
                    null,
                    null,
                    null,
                    filial.getId());
            var grupoProduto = new GrupoProduto(input, filial);
            grupoProduto.setId(1L);

            doReturn(Optional.of(grupoProduto)).when(grupoProdutoRepository).findById(1L);

            //Act
            var result = grupoProdutoService.getGrupoProdutoById(1L);

            //Assert
            assertNotNull(result);
            assertEquals(grupoProduto.getId(), 1L);
            verify(grupoProdutoRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw NotFoundException with invalid id")
        void shouldReturnNotFoundExceptionWithInvalidId() {
            //Arrange
            Long id = 1L;

            doThrow(NotFoundException.class).when(grupoProdutoRepository).findById(id);

            //Act
            //Assert
            assertThrows(NotFoundException.class, () -> grupoProdutoService.getGrupoProdutoById(id));
            verify(grupoProdutoRepository, times(1)).findById(1L);
        }
    }

    @Nested
    class updateGrupoProduto {

        @Test
        @DisplayName("Should update Grupo Produto with valid id")
        void shouldUpdateGrupoProdutoWithValidId() {
            //Arrange
            var filial = new Filial();
            filial.setId(1L);

            var grupoProduto = new GrupoProduto();
            grupoProduto.setId(1L);

            var inputDto = new GrupoProdutoRequestDto(
                    "Grupo Produto Updated",
                    TipoGrupoProduto.OUTROS,
                    BigDecimal.TEN.multiply(new BigDecimal("0.3")),
                    true,
                    SituacaoCadastro.ATIVO,
                    filial.getId());

            doReturn(Optional.of(grupoProduto)).when(grupoProdutoRepository).findById(1L);
            doReturn(Optional.of(filial)).when(filialService).buscaFilialPorId(inputDto.filialId());
            doReturn(grupoProduto).when(grupoProdutoRepository).save(any(GrupoProduto.class));

            //Act
            var result = grupoProdutoService.updateGrupoProduto(1L, inputDto);

            //Assert
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals(inputDto.nomeGrupo(), result.nomeGrupo());
            assertEquals(inputDto.tipoGrupo(), result.tipoGrupo());
            assertEquals(inputDto.margemLucro(), result.margemLucro());
            assertEquals(inputDto.atualizaPreco(), result.atualizaPreco());
            assertEquals(inputDto.situacaoCadastro(), result.situacaoCadastro());
            assertEquals(inputDto.filialId(), result.filialId());

            verify(grupoProdutoRepository, times(1)).findById(1L);
            verify(filialService, times(1)).buscaFilialPorId(inputDto.filialId());
            verify(grupoProdutoRepository, times(1)).save(grupoProdutoArgumentCaptor.capture());
        }
    }

    @Nested
    class deleteGrupoProduto {

        @Test
        @DisplayName("Should delete Grupo Produto with valid id")
        void shouldDeleteGrupoProdutoWithValidId() {
            //Arrange
            Long id = 1L;

            var filial = new Filial();
            filial.setId(1L);

            var input = new GrupoProdutoRequestDto(
                    "",
                    null,
                    null,
                    null,
                    null,
                    filial.getId());
            var grupoProduto = new GrupoProduto(input, filial);
            grupoProduto.setId(1L);
            doReturn(Optional.of(grupoProduto)).when(grupoProdutoRepository).findById(id);

            //Act
            grupoProdutoService.deleteGrupoProdutoById(id);

            //Assert
            verify(grupoProdutoRepository, times(1)).delete(grupoProduto);
        }
    }

    @Nested
    class getGrupoProdutoFilter {

        @Test
        @DisplayName("Should return filtered GrupoProduto page")
        void shouldReturnFilteredGrupoProdutoPage() {
            // Arrange
            String nomeGrupo = "Test";
            TipoGrupoProduto tipoGrupo = TipoGrupoProduto.OUTROS;
            Long idFilial = 1L;
            PageRequest pageable = PageRequest.of(0, 10);

            List<GrupoProdutoResponseDto> grupoProdutoList = Arrays.asList(
                    new GrupoProdutoResponseDto(
                            1L,
                            "Test Group 1",
                            TipoGrupoProduto.OUTROS,
                            BigDecimal.valueOf(0.15),
                            true,
                            SituacaoCadastro.ATIVO,
                            idFilial
                    ),
                    new GrupoProdutoResponseDto(
                            2L,
                            "Test Group 2",
                            TipoGrupoProduto.OUTROS,
                            BigDecimal.valueOf(0.20),
                            false,
                            SituacaoCadastro.ATIVO,
                            idFilial
                    )
            );
            Page<GrupoProdutoResponseDto> expectedPage = new PageImpl<>(grupoProdutoList, pageable, grupoProdutoList.size());

            when(grupoProdutoRepository.buscarGrupoProduto(nomeGrupo, tipoGrupo, idFilial, pageable))
                    .thenReturn(expectedPage);

            // Act
            Page<GrupoProdutoResponseDto> result = grupoProdutoService.getGrupoProdutoFilter(nomeGrupo, tipoGrupo, idFilial, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
            assertEquals(expectedPage.getContent().size(), result.getContent().size());
            assertEquals(expectedPage.getNumber(), result.getNumber());
            assertEquals(expectedPage.getSize(), result.getSize());

            GrupoProdutoResponseDto firstItem = result.getContent().getFirst();
            assertEquals(1L, firstItem.id());
            assertEquals("Test Group 1", firstItem.nomeGrupo());
            assertEquals(TipoGrupoProduto.OUTROS, firstItem.tipoGrupo());
            assertEquals(BigDecimal.valueOf(0.15), firstItem.margemLucro());
            assertTrue(firstItem.atualizaPreco());
            assertEquals(SituacaoCadastro.ATIVO, firstItem.situacaoCadastro());
            assertEquals(idFilial, firstItem.filialId());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(nomeGrupo, tipoGrupo, idFilial, pageable);
        }

        @Test
        @DisplayName("Should return empty page when no results found")
        void shouldReturnEmptyPageWhenNoResultsFound() {
            // Arrange
            String nomeGrupo = "NonExistent";
            TipoGrupoProduto tipoGrupo = null;
            Long idFilial = null;
            PageRequest pageable = PageRequest.of(0, 10);

            Page<GrupoProdutoResponseDto> expectedPage = Page.empty(pageable);

            when(grupoProdutoRepository.buscarGrupoProduto(nomeGrupo, tipoGrupo, idFilial, pageable))
                    .thenReturn(expectedPage);

            // Act
            Page<GrupoProdutoResponseDto> result = grupoProdutoService.getGrupoProdutoFilter(nomeGrupo, tipoGrupo, idFilial, pageable);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            assertEquals(0, result.getTotalElements());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(nomeGrupo, tipoGrupo, idFilial, pageable);
        }

        @Test
        @DisplayName("Should handle null filter parameters")
        void shouldHandleNullFilterParameters() {
            // Arrange
            String nomeGrupo = null;
            TipoGrupoProduto tipoGrupo = null;
            Long idFilial = null;
            PageRequest pageable = PageRequest.of(0, 10);

            List<GrupoProdutoResponseDto> grupoProdutoList = Arrays.asList(
                    new GrupoProdutoResponseDto(
                            1L,
                            "Test Group 1",
                            TipoGrupoProduto.OUTROS,
                            BigDecimal.valueOf(0.15),
                            true,
                            SituacaoCadastro.ATIVO,
                            idFilial
                    ),
                    new GrupoProdutoResponseDto(
                            2L,
                            "Test Group 2",
                            TipoGrupoProduto.OUTROS,
                            BigDecimal.valueOf(0.20),
                            false,
                            SituacaoCadastro.ATIVO,
                            idFilial
                    )
            );
            Page<GrupoProdutoResponseDto> expectedPage = new PageImpl<>(grupoProdutoList, pageable, grupoProdutoList.size());

            when(grupoProdutoRepository.buscarGrupoProduto(nomeGrupo, tipoGrupo, idFilial, pageable))
                    .thenReturn(expectedPage);

            // Act
            Page<GrupoProdutoResponseDto> result = grupoProdutoService.getGrupoProdutoFilter(nomeGrupo, tipoGrupo, idFilial, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
            assertEquals(expectedPage.getContent().size(), result.getContent().size());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(nomeGrupo, tipoGrupo, idFilial, pageable);
        }
    }

    @Nested
    class alteraGrupoProdutoById {

        @Test
        @DisplayName("Should change situaçãoCadastro to ativo status with valid id")
        void shouldChangeSituacaoCadastroToAtivoStatusWithValidId() {
            //Arrange
            var filial = new Filial();
            filial.setId(1L);
            Long id = 1L;
            var input = new GrupoProdutoRequestDto(
                    "GrupoProduto",
                    TipoGrupoProduto.OUTROS,
                    BigDecimal.TEN.multiply(new BigDecimal("0.3")),
                    true,
                    SituacaoCadastro.INATIVO,
                    filial.getId());
            var grupoProduto = new GrupoProduto(input, filial);
            grupoProduto.setId(1L);

            doReturn(Optional.of(grupoProduto)).when(grupoProdutoRepository).findById(id);

            //Act
            var result = grupoProdutoService.alteraGrupoProdutoById(id, true);

            //Assert
            assertNotNull(result);
            assertEquals(SituacaoCadastro.ATIVO, result.situacaoCadastro());
            verify(grupoProdutoRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should change situaçãoCadastro to inativo status with valid id")
        void shouldChangeSituacaoCadastroToInativoStatusWithValidId() {
            //Arrange
            var filial = new Filial();
            filial.setId(1L);
            Long id = 1L;
            var input = new GrupoProdutoRequestDto(
                    "GrupoProduto",
                    TipoGrupoProduto.OUTROS,
                    BigDecimal.TEN.multiply(new BigDecimal("0.3")),
                    true,
                    SituacaoCadastro.ATIVO,
                    filial.getId());
            var grupoProduto = new GrupoProduto(input, filial);
            grupoProduto.setId(1L);

            doReturn(Optional.of(grupoProduto)).when(grupoProdutoRepository).findById(id);

            //Act
            var result = grupoProdutoService.alteraGrupoProdutoById(id, false);

            //Assert
            assertNotNull(result);
            assertEquals(SituacaoCadastro.INATIVO, result.situacaoCadastro());
            verify(grupoProdutoRepository, times(1)).findById(1L);
        }
    }

    @Nested
    class buscaGrupoProdutoPorId {

        @Test
        @DisplayName("Should return GrupoProduto with valid id")
        void shouldReturnGrupoProdutoWithValidId() {
            //Arrange
            var grupoProduto = new GrupoProduto();
            grupoProduto.setId(1L);

            doReturn(Optional.of(grupoProduto)).when(grupoProdutoRepository).findById(1L);

            //Act
            var result = grupoProdutoService.buscaGrupoProdutoPorId(1L);

            //Assert
            assertTrue(result.isPresent());
            assertEquals(grupoProduto.getId(), 1L);
            verify(grupoProdutoRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should return Optional.empty with invalid id")
        void shouldReturnOptionalEmptyWithInvalidId() {
            //Arrange
            Long id = 1L;
            doReturn(Optional.empty()).when(grupoProdutoRepository).findById(id);

            //Act

            var result = grupoProdutoService.buscaGrupoProdutoPorId(id);
            //Assert
            assertTrue(result.isEmpty());
            verify(grupoProdutoRepository, times(1)).findById(1L);
        }
    }

    @Nested
    class grupoProdutoById {

        @Test
        @DisplayName("Should return GrupoProduto with valid id")
        void shouldReturnGrupoProdutoWithValidId() {
            //Arrange
            var grupoProduto = new GrupoProduto();
            grupoProduto.setId(1L);

            doReturn(Optional.of(grupoProduto)).when(grupoProdutoRepository).findById(1L);

            //Act
            var result = grupoProdutoService.grupoProdutoById(1L);

            //Assert
            assertNotNull(result);

            assertEquals(1L, result.getId());

            verify(grupoProdutoRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should throw NotFoundException with invalid id")
        void shouldThrowNotFoundExceptionWithInvalidId() {
            //Arrange
            Long id = 1L;
            doThrow(NotFoundException.class).when(grupoProdutoRepository).findById(id);

            //Assert Act
            assertThrows(NotFoundException.class, () -> grupoProdutoService.grupoProdutoById(id));
            verify(grupoProdutoRepository, times(1)).findById(1L);
        }
    }
}