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
import triersistemas.estagio_back_end.dto.request.FornecedorPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.repository.FornecedorRepository;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.validators.CnpjValidator;
import triersistemas.estagio_back_end.validators.FoneValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FornecedorServiceImplTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private FilialService filialService;

    @Mock
    private CnpjValidator cnpjValidator;

    @Mock
    private FoneValidator foneValidator;

    @InjectMocks
    private FornecedorServiceImpl fornecedorService;

    private final Filial filial = new Filial();
    private Fornecedor fornecedor;
    private FornecedorRequestDto requestDto;
    private final Long fornecedorId  = 1L;


    @Captor
    ArgumentCaptor<Fornecedor> fornecedorArgumentCaptor;

    @BeforeEach
    void setUp() {
        Long filialId = 1L;
        filial.setId(filialId);

        requestDto = new FornecedorRequestDto(
                "Nome",
                "Razão",
                "52.353.295/0001-83",
                "(63) 3245-6887",
                "email@email.com",
                SituacaoCadastro.ATIVO,
                filial.getId());
        fornecedor = new Fornecedor(requestDto, filial);
        fornecedor.setId(fornecedorId);
    }

    @Nested
    class addFornecedorTest {

        @Test
        @DisplayName("Deve retornar um FornecedorResponse e salvar no banco com os dados preenchidos")
        void addFornecedorTest_V1() {

            doReturn(filial).when(filialService).findById(requestDto.filialId());
            doReturn(fornecedor).when(fornecedorRepository).save(fornecedorArgumentCaptor.capture());
            doNothing().when(cnpjValidator).validateCnpjPostFornecedor(requestDto.cnpj());
            doNothing().when(foneValidator).validateFone(requestDto.telefone());

            var actual = fornecedorService.addFornecedor(requestDto);

            var capturedFornecedor = fornecedorArgumentCaptor.getValue();
            capturedFornecedor.setId(fornecedorId);

            var expected = new FornecedorResponseDto(capturedFornecedor);

            assertNotNull(actual);
            assertEquals(expected, actual);

            verify(filialService, times(1)).findById(requestDto.filialId());
            verify(fornecedorRepository, times(1)).save(fornecedor);
            verify(cnpjValidator, times(1)).validateCnpjPostFornecedor(requestDto.cnpj());
            verify(foneValidator, times(1)).validateFone(requestDto.telefone());


        }
    }

    @Nested
    class updateFornecedorTest {

        @Test
        @DisplayName("Deve retornar um FornecedorResponse e atualizar o banco de dados")
        void updateFornecedorTest_V1() {
            var newRequestDto = new FornecedorRequestDto(
                    "Nome Update",
                    "Razão Update",
                    "52.353.295/0001-83",
                    "(63) 3245-6882",
                    "emailupdated@email.com",
                    SituacaoCadastro.INATIVO,
                    filial.getId());

            doReturn(Optional.of(fornecedor)).when(fornecedorRepository).findById(fornecedorId);
            doReturn(Optional.of(filial)).when(filialService).buscaFilialPorId(requestDto.filialId());
            doReturn(fornecedor).when(fornecedorRepository).save(fornecedorArgumentCaptor.capture());

            var actual = fornecedorService.updateFornecedor(fornecedorId, newRequestDto);
            var expected = new FornecedorResponseDto(fornecedorArgumentCaptor.getValue());

            assertNotNull(actual);
            assertEquals(expected, actual);

            verify(fornecedorRepository, times(1)).findById(fornecedorId);
            verify(fornecedorRepository, times(1)).save(fornecedor);

        }

        @Test
        @DisplayName("Deve retornar um FornecedorResponse e atualizar os dados com cnpj diferente")
        void updateFornecedorTest_V2() {
            var newRequestDto = new FornecedorRequestDto(
                    "Nome Update",
                    "Razão Update",
                    "52.353.295/0001-84",
                    "(63) 3245-6882",
                    "emailupdated@email.com",
                    SituacaoCadastro.INATIVO,
                    filial.getId());


            doReturn(Optional.of(fornecedor)).when(fornecedorRepository).findById(fornecedorId);
            doReturn(Optional.of(filial)).when(filialService).buscaFilialPorId(requestDto.filialId());
            doReturn(fornecedor).when(fornecedorRepository).save(fornecedorArgumentCaptor.capture());
            doNothing().when(cnpjValidator).validateCnpjUpdateFornecedor(newRequestDto.cnpj(), newRequestDto.filialId());

            var actual = fornecedorService.updateFornecedor(fornecedorId, newRequestDto);
            var expected = new FornecedorResponseDto(fornecedorArgumentCaptor.getValue());

            assertNotNull(actual);
            assertEquals(expected, actual);

            verify(fornecedorRepository, times(1)).findById(fornecedorId);
            verify(fornecedorRepository, times(1)).save(fornecedor);
            verify(cnpjValidator, times(1)).validateCnpjUpdateFornecedor(newRequestDto.cnpj(), newRequestDto.filialId());

        }
    }

    @Nested
    class deleteFornecedorTest {

        @Test
        @DisplayName("Deve deletar e retornar um fornecedorResponse encontrado pelo id")
        void deleteFornecedorTest_V1() {

            doReturn(Optional.of(fornecedor)).when(fornecedorRepository).findById(fornecedorId);
            doNothing().when(fornecedorRepository).delete(fornecedorArgumentCaptor.capture());

            var actual = fornecedorService.deleteFornecedor(fornecedorId);
            var expected = new FornecedorResponseDto(fornecedorArgumentCaptor.getValue());

            assertEquals(expected, actual);

            verify(fornecedorRepository, times(1)).findById(fornecedorId);
            verify(fornecedorRepository, times(1)).delete(fornecedorArgumentCaptor.capture());
        }
    }

    @Nested
    class getFornecedorByIdTest {

        @Test
        @DisplayName("Deve retornar um fornecedor pelo id")
        void getFornecedorByIdTest_V1() {

            doReturn(Optional.of(fornecedor)).when(fornecedorRepository).findById(fornecedorId);

            var actual = fornecedorService.getFornecedorById(fornecedorId);
            var expected = new FornecedorResponseDto(fornecedor);

            assertNotNull(actual);
            assertEquals(expected, actual);

            verify(fornecedorRepository, times(1)).findById(fornecedorId);
        }
    }

    @Nested
    class getFornecedorPagedTest {

        @Test
        @DisplayName("Deve retornar fornecedores que batem com o nome,cnpj e situação cadastro dados, usando Pageable")
        void getFornecedorPagedTest_V2() {
            PageRequest pageable = PageRequest.of(0, 5);
            FornecedorPagedRequestDto fornecedorPaged = new FornecedorPagedRequestDto("nome", null, "52.353.295/0001-83",null, null, SituacaoCadastro.ATIVO,List.of(), Map.of());
            String cnpj = "52.353.295/0001-83";
            String name = "Nome";
            Long filialId = 1L;
            SituacaoCadastro situacaoCadastro = SituacaoCadastro.ATIVO;

            FornecedorResponseDto responseDto = new FornecedorResponseDto(fornecedor);
            Page<FornecedorResponseDto> expected = new PageImpl<>(List.of(responseDto));

            doReturn(new PageImpl<>(List.of(responseDto))).when(fornecedorRepository).buscarFornecedores(filialId, fornecedorPaged,pageable);

            Page<FornecedorResponseDto> actual = fornecedorService.getFornecedorPaged(filialId, fornecedorPaged,pageable);

            assertEquals(expected.getTotalElements(), actual.getTotalElements());
            assertEquals(expected.getTotalPages(), actual.getTotalPages());
            assertEquals(expected.getContent().getFirst(), actual.getContent().getFirst());

            verify(fornecedorRepository, times(1)).buscarFornecedores(filialId, fornecedorPaged,pageable);
        }
    }
}