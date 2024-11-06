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
import triersistemas.estagio_back_end.dto.request.GrupoProdutoPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoRequestDto;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoChartDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.repository.GrupoProdutoRepository;
import triersistemas.estagio_back_end.services.FilialService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GrupoProdutoServiceImplTest {

    @Mock
    private GrupoProdutoRepository grupoProdutoRepository;

    @Mock
    private FilialService filialService;

    @InjectMocks
    private GrupoProdutoServiceImpl grupoProdutoService;

    private GrupoProdutoRequestDto requestDto;
    private GrupoProduto grupoProduto;
    private final Filial filial = new Filial();
    private final Long filialId = 1L;
    private final Long grupoProdutoId = 1L;

    @Captor
    ArgumentCaptor<GrupoProduto> grupoProdutoArgumentCaptor;

    @BeforeEach
    public void setup() {
        filial.setId(filialId);
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
        @DisplayName("Deve retornar um grupoProdutoResponse e salva no banco, com os dados preenchidos")
        void addGrupoProdutoTest_V1() {

            doReturn(filial).when(filialService).findById(filialId);
            doReturn(grupoProduto).when(grupoProdutoRepository).save(grupoProdutoArgumentCaptor.capture());

            var actual = grupoProdutoService.addGrupoProduto(requestDto);
            var expected = new GrupoProdutoResponseDto(grupoProdutoArgumentCaptor.getValue());

            assertNotNull(actual);
            assertEquals(expected, actual);

            verify(filialService, times(1)).findById(filialId);
            verify(grupoProdutoRepository, times(1)).save(grupoProdutoArgumentCaptor.capture());
        }
    }

    @Nested
    class updateGrupoProdutoTest {

        @Test
        @DisplayName("Deve retornar um grupoProdutoResponse e atualizar o banco de dados")
        void updateGrupoProdutoTest_V1() {

            var newRequestDto = new GrupoProdutoRequestDto(
                    "Nome Update",
                    TipoGrupoProduto.BEBIDA,
                    BigDecimal.ONE,
                    true,
                    SituacaoCadastro.ATIVO,
                    filial.getId());

            doReturn(Optional.of(filial)).when(filialService).buscaFilialPorId(filialId);
            doReturn(Optional.of(grupoProduto)).when(grupoProdutoRepository).findById(grupoProdutoId);
            doReturn(grupoProduto).when(grupoProdutoRepository).save(grupoProdutoArgumentCaptor.capture());

            var actual = grupoProdutoService.updateGrupoProduto(1L, newRequestDto);
            var expected = new GrupoProdutoResponseDto(grupoProdutoArgumentCaptor.getValue());

            assertNotNull(actual);
            assertEquals(expected, actual);

            verify(filialService, times(1)).buscaFilialPorId(filialId);
            verify(grupoProdutoRepository, times(1)).findById(grupoProdutoId);
            verify(grupoProdutoRepository, times(1)).save(grupoProdutoArgumentCaptor.capture());
        }
    }

    @Nested
    class deleteGrupoProdutoTest {

        @Test
        @DisplayName("Deve deletar e retornar um grupoProdutoResponse encontrado pelo id")
        void deleteGrupoProdutoTest_V1() {

            doReturn(Optional.of(grupoProduto)).when(grupoProdutoRepository).findById(grupoProdutoId);
            doNothing().when(grupoProdutoRepository).delete(grupoProdutoArgumentCaptor.capture());

            var actual = grupoProdutoService.deleteGrupoProduto(grupoProdutoId);
            var expected = new GrupoProdutoResponseDto(grupoProdutoArgumentCaptor.getValue());

            assertEquals(expected, actual);

            verify(grupoProdutoRepository, times(1)).findById(grupoProdutoId);
            verify(grupoProdutoRepository, times(1)).delete(grupoProdutoArgumentCaptor.capture());
        }
    }

    @Nested
    class getProdutos{
        @Test
        @DisplayName("deve retornar uma lista de GrupoProdutoChartDto")
        void getProdutoTest_V1(){
            Produto produto = new Produto(new ProdutoRequestDto("7894561237899", "teste", null, 1L, TipoProduto.NAO_DEFINIDO, Apresentacao.OUTROS, BigDecimal.TEN, true, BigDecimal.TEN, SituacaoCadastro.ATIVO),grupoProduto );
            var produtos = List.of(produto);
            filial.setProdutos(produtos);
            grupoProduto.setProdutos(produtos);
            var gruposProduto = List.of(grupoProduto);
            filial.setGrupoProdutos(gruposProduto);

            doReturn(filial).when(filialService).findById(filialId);
            var actual = grupoProdutoService.getProdutos(filialId);
            assertEquals(gruposProduto.stream().map(GrupoProdutoChartDto::new).toList(),actual);
        }
    }

    @Nested
    class getGrupoProdutoFilterTest {

        @Test
        @DisplayName("Deve retornar grupos de produtos que batem com o nome dado, em uma lista")
        void getGrupoProdutoFilterTest_V1() {
            String name = "Nome";

            var responseDto = new GrupoProdutoResponseDto(grupoProduto);
            List<GrupoProdutoResponseDto> expected = List.of(responseDto);

            doReturn(List.of(responseDto)).when(grupoProdutoRepository).buscarGrupoProduto(name, filialId);

            List<GrupoProdutoResponseDto> actual = grupoProdutoService.getGrupoProdutoFilter(name, filialId);

            assertEquals(1, actual.size());
            assertEquals(expected.getFirst(), actual.getFirst());
            assertEquals(name, actual.getFirst().nomeGrupo());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(name, filialId);
        }
    }

    @Nested
    class getGrupoProdutoPagedTest {

        @Test
        @DisplayName("Deve retornar grupos de produto que batem com o requestDto, usando Pageable")
        void getGrupoProdutoPagedTest_V1() {
            Pageable pageable = PageRequest.of(0, 5);

            var newRequestDto = new GrupoProdutoPagedRequestDto(
                    "Nome",
                    TipoGrupoProduto.BEBIDA,
                    null,
                    true,
                    SituacaoCadastro.ATIVO,
                    null,
                    null);

            var responseDto = new GrupoProdutoResponseDto(grupoProduto);
            Page<GrupoProdutoResponseDto> expected = new PageImpl<>(List.of(responseDto));

            doReturn(new PageImpl<>(List.of(responseDto))).when(grupoProdutoRepository).buscarGrupoProduto(newRequestDto, filialId, pageable);

            Page<GrupoProdutoResponseDto> actual = grupoProdutoService.getGrupoProdutoPaged(newRequestDto, filialId, pageable);

            assertEquals(expected.getTotalElements(), actual.getTotalElements());
            assertEquals(expected.getTotalPages(), actual.getTotalPages());
            assertEquals(expected.getContent().getFirst(), actual.getContent().getFirst());

            verify(grupoProdutoRepository, times(1)).buscarGrupoProduto(newRequestDto, filialId, pageable);
        }
    }

    @Nested
    class getGrupoProdutoByIdTest {

        @Test
        @DisplayName("Deve retornar um grupo de produto pelo id")
        void getGrupoProdutoByIdTest_V1() {

            doReturn(Optional.of(grupoProduto)).when(grupoProdutoRepository).findById(grupoProdutoId);

            var actual = grupoProdutoService.getGrupoProdutoById(grupoProdutoId);
            var expected = new GrupoProdutoResponseDto(grupoProduto);

            assertNotNull(actual);
            assertEquals(expected, actual);

            verify(grupoProdutoRepository, times(1)).findById(grupoProdutoId);
        }
    }

}