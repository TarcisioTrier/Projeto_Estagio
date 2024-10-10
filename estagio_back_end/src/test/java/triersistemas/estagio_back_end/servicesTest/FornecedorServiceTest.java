package triersistemas.estagio_back_end.servicesTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.FornecedorRepository;
import triersistemas.estagio_back_end.services.FilialService;
import triersistemas.estagio_back_end.services.impl.FornecedorServiceImpl;
import triersistemas.estagio_back_end.validators.CnpjValidator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FornecedorServiceTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private FilialService filialService;

    @Mock
    private CnpjValidator cnpjValidator;

    @InjectMocks
    private FornecedorServiceImpl service;

    private Fornecedor fornecedor;
    private FornecedorRequestDto fornecedorDto;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Teste Validator FornecedorDto - Blank")
    public void blankPatternFornecedorDto_Validator() {
        fornecedorDto = new FornecedorRequestDto(
                "",
                "",
                "",
                "",
                "",
                null,
                1L);

        Set<ConstraintViolation<FornecedorRequestDto>> violations = validator.validate(fornecedorDto);

        assertEquals(5, violations.size());
    }

    @Test
    @DisplayName("Teste Validator Fornecedor - Blank")
    public void blankPatternFornecedor_Validator() {
        Filial filial = new Filial();
        filial.setId(1L);
        fornecedorDto = new FornecedorRequestDto(
                "",
                "",
                "",
                "",
                "",
                null,
                null);
        fornecedor = new Fornecedor(fornecedorDto, filial);

        Set<ConstraintViolation<Fornecedor>> violations = validator.validate(fornecedor);

        assertEquals(5, violations.size());
    }

    @Test
    @DisplayName("Teste addFornecedor - Com filial")
    public void addFornecedor_ReturnFornecedorWithFilial() {
        Filial filial = new Filial();
        filial.setId(1L);

        fornecedorDto = new FornecedorRequestDto(
                "Nome Fantasia Teste",
                "Razão Social Teste",
                "12345678901234",
                "9999999999",
                "teste@teste.com",
                SituacaoCadastro.ATIVO,
                1L);

        fornecedor = new Fornecedor(fornecedorDto, filial);

        when(filialService.findById(1L)).thenReturn(filial);
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedor);

        FornecedorResponseDto responseDto = service.addFornecedor(fornecedorDto);

        assertNotNull(responseDto);
        assertEquals(fornecedorDto.nomeFantasia(), responseDto.nomeFantasia());
        assertEquals(fornecedorDto.razaoSocial(), responseDto.razaoSocial());
        assertEquals(fornecedorDto.cnpj(), responseDto.cnpj());
        assertEquals(fornecedorDto.telefone(), responseDto.telefone());
        assertEquals(fornecedorDto.email(), responseDto.email());
        assertEquals(fornecedorDto.situacaoCadastro(), responseDto.situacaoCadastro());
        assertEquals(fornecedorDto.filialId(), responseDto.filialId());

        verify(cnpjValidator).validateCnpj(fornecedorDto.cnpj());
        verify(filialService).findById(1L);
        verify(fornecedorRepository).save(any(Fornecedor.class));
    }

    @Test
    @DisplayName("Teste addFornecedor - Sem filial")
    public void AddFornecedor_ReturnNotFound() {
        Filial filial = null;
        fornecedorDto = new FornecedorRequestDto(
                "Nome Fantasia Teste",
                "Razão Social Teste",
                "12345678901234",
                "9999999999",
                "teste@teste.com",
                SituacaoCadastro.ATIVO,
                2L);
        fornecedor = new Fornecedor(fornecedorDto, filial);;

        assertThrows(NotFoundException.class, () -> service.addFornecedor(fornecedorDto));

    }

    @Test
    @DisplayName("Teste getFornecedorById - retorna Fornecedor")
    public void getFornecedorById_ReturnFornecedor() {
        Filial filial = new Filial();
        filial.setId(1L);
        fornecedorDto = new FornecedorRequestDto(
                "Nome Fantasia Teste",
                "Razão Social Teste",
                "12345678901234",
                "9999999999",
                "teste@teste.com",
                SituacaoCadastro.ATIVO,
                1L);
        fornecedor = new Fornecedor(fornecedorDto, filial);
        fornecedor.setId(1L);

        fornecedorRepository.save(fornecedor);
        lenient().when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedor);
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));

        FornecedorResponseDto responseDto = service.getFornecedorById(1L);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.id());

        verify(fornecedorRepository).findById(1L);
    }

    @Test
    @DisplayName("Teste getFornecedorById - NotFoundException")
    public void getFornecedorById_ReturnNotFound() {
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getFornecedorById(1L));

        verify(fornecedorRepository).findById(1L);
    }

    @Test
    @DisplayName("Teste updateFornecedor - altera dados de fornecedor cadastrado")
    public void updateFornecedor_ReturnFornecedor() {
        Filial filial = new Filial();
        filial.setId(2L);

        fornecedor = new Fornecedor();
        fornecedor.setId(1L);

        fornecedorDto = new FornecedorRequestDto(
                "Nome Fantasia Alterado",
                "Razão Social Alterada",
                "12345678901234",
                "9999999999",
                "teste@teste.com",
                SituacaoCadastro.ATIVO,
                2L);

        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(filialService.findById(2L)).thenReturn(filial);
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedor);

        FornecedorResponseDto responseDto = service.updateFornecedor(1L, fornecedorDto);

        assertNotNull(responseDto);
        assertEquals(fornecedorDto.nomeFantasia(), responseDto.nomeFantasia());
        assertEquals(fornecedorDto.razaoSocial(), responseDto.razaoSocial());
        assertEquals(fornecedorDto.cnpj(), responseDto.cnpj());
        assertEquals(fornecedorDto.telefone(), responseDto.telefone());
        assertEquals(fornecedorDto.email(), responseDto.email());
        assertEquals(fornecedorDto.situacaoCadastro(), responseDto.situacaoCadastro());
        assertEquals(fornecedorDto.filialId(), responseDto.filialId());

        verify(fornecedorRepository).findById(1L);
        verify(filialService).findById(2L);
        verify(fornecedorRepository).save(any(Fornecedor.class));
    }

    @Test
    @DisplayName("Teste updateFornecedor - NotFoundException")
    public void updateFornecedor_ReturnNotFound() {
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateFornecedor(1L, fornecedorDto));

        verify(fornecedorRepository).findById(1L);
        verify(fornecedorRepository, never()).save(any(Fornecedor.class));
    }

    @Test
    @DisplayName("Teste getFornecedorFilter - retorna lista de fornecedorResponseDto paginada")
    public void getFornecedorFilter_ReturnFornecedor() {
        String nomeFiltro = "Nome Teste";
        String cnpjFiltro = "12345678901234";
        SituacaoCadastro situacaoCadastro = SituacaoCadastro.ATIVO;

        PageRequest pageable = PageRequest.of(0, 10);

        FornecedorResponseDto dto1 = new FornecedorResponseDto(1L, "Fornecedor 1", "Razao1", cnpjFiltro, "12345678910", "fornecedor1@fornecedor1.com", SituacaoCadastro.ATIVO, 1L);
        FornecedorResponseDto dto2 = new FornecedorResponseDto(2L, "Fornecedor 2", "Razao2", "09876543211234", "0987654321", "fornecedor2@fornecedor2.com", SituacaoCadastro.ATIVO, 2L);

        Page<FornecedorResponseDto> page = new PageImpl<>(Arrays.asList(dto1, dto2), pageable, 2);

        when(fornecedorRepository.buscarFornecedores(nomeFiltro, cnpjFiltro, situacaoCadastro, pageable)).thenReturn(page);

        Page<FornecedorResponseDto> result = service.getFornecedorFilter(nomeFiltro, cnpjFiltro, situacaoCadastro, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getContent().size());
        assertEquals("Fornecedor 1", result.getContent().get(0).nomeFantasia());
        assertEquals("Fornecedor 2", result.getContent().get(1).nomeFantasia());

        verify(fornecedorRepository).buscarFornecedores(nomeFiltro, cnpjFiltro, situacaoCadastro, pageable);
    }

    @Test
    @DisplayName("Teste getFornecedorFilter - Retorna lista vazia")
    public void getFornecedorFilter_ReturnEmpty() {
        String nomeFiltro = "Nome Inexistente";
        String cnpjFiltro = "99999999999999";
        SituacaoCadastro situacaoCadastro = SituacaoCadastro.INATIVO;

        PageRequest pageable = PageRequest.of(0, 10);

        Page<FornecedorResponseDto> page = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(fornecedorRepository.buscarFornecedores(nomeFiltro, cnpjFiltro, situacaoCadastro, pageable)).thenReturn(page);

        Page<FornecedorResponseDto> result = service.getFornecedorFilter(nomeFiltro, cnpjFiltro, situacaoCadastro, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertTrue(result.getContent().isEmpty());

        verify(fornecedorRepository).buscarFornecedores(nomeFiltro, cnpjFiltro, situacaoCadastro, pageable);
    }

    @Test
    @DisplayName("Teste alteraSituacao - altera a situação do fornecedor cadastrado")
    public void alteraSituacao_ChangeSituacaoCadastro() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1L);
        fornecedor.setSituacaoCadastro(SituacaoCadastro.ATIVO);

        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedor);

        service.alteraSituacao(1L);

        assertEquals(SituacaoCadastro.INATIVO, fornecedor.getSituacaoCadastro());

        verify(fornecedorRepository).findById(1L);
        verify(fornecedorRepository).save(any(Fornecedor.class));
    }
}