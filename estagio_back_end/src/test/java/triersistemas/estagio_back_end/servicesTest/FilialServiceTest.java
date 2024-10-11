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
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.dto.response.FilialResponseDto;
import triersistemas.estagio_back_end.entity.Enderecos;
import triersistemas.estagio_back_end.entity.Filial;
import triersistemas.estagio_back_end.enuns.SituacaoContrato;
import triersistemas.estagio_back_end.exceptions.NotFoundException;
import triersistemas.estagio_back_end.repository.FilialRepository;
import triersistemas.estagio_back_end.services.impl.FilialServiceImpl;
import triersistemas.estagio_back_end.validators.CnpjValidator;
import triersistemas.estagio_back_end.validators.EnderecosValidator;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FilialServiceTest {

    @Mock
    private FilialRepository filialRepository;

    @Mock
    private EnderecosValidator enderecosValidator;

    @Mock
    private CnpjValidator cnpjValidator;

    @InjectMocks
    private FilialServiceImpl service;

    private Filial filial;
    private Enderecos enderecos;
    private FilialRequestDto filialDto;
    private EnderecosDto enderecosDto;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Teste Validator EndereçoDto - Blank")
    public void BlankPatternEnderecoDto_Validator() {
        enderecosDto = new EnderecosDto(
                "",
                123,
                "Complemento",
                "",
                "",
                "",
                "");

        Set<ConstraintViolation<EnderecosDto>> violations = validator.validate(enderecosDto);

        assertEquals(5, violations.size());  // Expect one violation
        ConstraintViolation<EnderecosDto> violation = violations.iterator().next();
    }

    @Test
    @DisplayName("Teste Validator FilialDto - Blank")
    public void BlankPatternFilialDto_Validator() {
        filialDto = new FilialRequestDto(
                "",
                "",
                "",
                "",
                "",
                null,
                null);

        Set<ConstraintViolation<FilialRequestDto>> violations = validator.validate(filialDto);

        assertEquals(5, violations.size());  // Expect one violation
        ConstraintViolation<FilialRequestDto> violation = violations.iterator().next();
    }

    @Test
    @DisplayName("Teste Validator Filial - Blank")
    public void BlankPatternFilial_Validator() {
        filialDto = new FilialRequestDto(
                "",
                "",
                "",
                "",
                "",
                null,
                null);

        filial = new Filial(filialDto);

        Set<ConstraintViolation<Filial>> violations = validator.validate(filial);

        assertEquals(5, violations.size());  // Expect one violation
        ConstraintViolation<Filial> violation = violations.iterator().next();
    }

    @Test
    @DisplayName("Teste Validator Endereço - Blank")
    public void BlankPatternEndereços_Validator() {
        enderecosDto = new EnderecosDto(
                "",
                123,
                "Complemento",
                "",
                "",
                "",
                "");
        enderecos = new Enderecos(enderecosDto);

        Set<ConstraintViolation<Enderecos>> violations = validator.validate(enderecos);

        assertEquals(5, violations.size());  // Expect one violation
        ConstraintViolation<Enderecos> violation = violations.iterator().next();
    }

    @Test
    @DisplayName("Teste addFilial - Com endereço ")
    public void AddFilial_ReturnFilialWithAdress() {
        enderecosDto = new EnderecosDto(
                "Rua teste",
                123,
                "Complemento",
                "Cidade Teste",
                "UF Teste",
                "00000-000",
                "Bairro Teste");
        enderecos = new Enderecos(enderecosDto);
        filialDto = new FilialRequestDto(
                "Nome Fantasia Teste",
                "Razão Social Teste",
                "12345678901234",
                "9999999999",
                "teste@Teste.com",
                SituacaoContrato.ATIVO,
                enderecosDto);

        when(enderecosValidator.validateEndereco(filialDto.endereco())).thenReturn(enderecos);

        filial = new Filial(filialDto);

        when(filialRepository.save(filial)).thenReturn(filial);

        FilialResponseDto responseDto = service.addFilial(filialDto);

        assertNotNull(responseDto);
        assertEquals(filialDto.nomeFantasia(), responseDto.nomeFantasia());
        assertEquals(filialDto.razaoSocial(), responseDto.razaoSocial());
        assertEquals(filialDto.cnpj(), responseDto.cnpj());
        assertEquals(filialDto.telefone(), responseDto.telefone());
        assertEquals(filialDto.email(), responseDto.email());
        assertEquals(filialDto.situacaoContrato(), responseDto.situacaoContrato());

        assertNotNull(responseDto.endereco());
        assertEquals(enderecosDto.logradouro(), responseDto.endereco().logradouro());
        assertEquals(enderecosDto.numero(), responseDto.endereco().numero());
        assertEquals(enderecosDto.complemento(), responseDto.endereco().complemento());
        assertEquals(enderecosDto.localidade(), responseDto.endereco().localidade());
        assertEquals(enderecosDto.estado(), responseDto.endereco().estado());
        assertEquals(enderecosDto.cep(), responseDto.endereco().cep());
        assertEquals(enderecosDto.bairro(), responseDto.endereco().bairro());

        verify(cnpjValidator).validateCnpj(filialDto.cnpj());
        verify(enderecosValidator).validateEndereco(filialDto.endereco());
        verify(filialRepository).save(filial);
    }

    @Test
    @DisplayName("Teste addFilial - sem endereço")
    public void addFilial_ReturnFilialWithNoAdress() {
        filialDto = new FilialRequestDto(
                "Nome Fantasia Teste",
                "Razão Social Teste",
                "12345678901235",
                "8888888888",
                "teste2@Teste.com",
                SituacaoContrato.ATIVO,
                null);
        filial = new Filial(filialDto);
        filial.setEndereco(null);
        filial.setId(1L);

        when(filialRepository.save(any(Filial.class))).thenReturn(filial);

        FilialResponseDto responseDto = service.addFilial(filialDto);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.id());
        assertEquals(filialDto.nomeFantasia(), responseDto.nomeFantasia());
        assertEquals(filialDto.razaoSocial(), responseDto.razaoSocial());
        assertEquals(filialDto.cnpj(), responseDto.cnpj());
        assertEquals(filialDto.telefone(), responseDto.telefone());
        assertEquals(filialDto.email(), responseDto.email());
        assertEquals(filialDto.situacaoContrato(), responseDto.situacaoContrato());
    }

    @Test
    @DisplayName("Teste findById - retorna Filial")
    public void findById_ReturnFilial() {
        filialDto = new FilialRequestDto(
                "Nome Fantasia Teste",
                "Razão Social Teste",
                "12345678901235",
                "8888888888",
                "teste2@Teste.com",
                SituacaoContrato.ATIVO,
                null);
        filial = new Filial(filialDto);
        filial.setEndereco(null);
        filial.setId(1L);

        filialRepository.save(filial);

        lenient().when(filialRepository.save(any(Filial.class))).thenReturn(filial);
        when(filialRepository.findById(1L)).thenReturn(Optional.of(filial));

        Filial filialEncontrada = service.findById(1L);

        assertNotNull(filialEncontrada);
    }

    @Test
    @DisplayName("Teste findById - NotFoundException")
    public void findById_ReturnNotFound() {
        when(filialRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.findById(1L);
        });

        assertEquals("Filial não encontrada", exception.getMessage());

        verify(filialRepository).findById(1L);
    }

    @Test
    @DisplayName("Teste deleteFilial - retorna filial e deletando")
    public void deleteFilial_ReturnDeletedFilial() {
        filialDto = new FilialRequestDto(
                "Nome Fantasia Teste",
                "Razão Social Teste",
                "12345678901235",
                "8888888888",
                "teste2@Teste.com",
                SituacaoContrato.ATIVO,
                null);
        filial = new Filial(filialDto);
        filial.setEndereco(null);
        filial.setId(1L);

        when(filialRepository.findById(1L)).thenReturn(Optional.of(filial));


        service.deleteFilial(1L);


        verify(filialRepository).delete(filial);


        verify(filialRepository).findById(1L);
    }

    @Test
    @DisplayName("Teste deleteFilial - NotFoundException")
    public void deleteFilial_ReturnNotFound() {
        when(filialRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.deleteFilial(1L);
        });

        assertEquals("Filial não encontrada", exception.getMessage());

        verify(filialRepository, never()).delete(any(Filial.class));
        verify(filialRepository).findById(1L);
    }

    @Test
    @DisplayName("Teste updateFilial - altera dados de filial cadastrada")
    public void updateFilial_ReturnFilial() {
        enderecosDto = new EnderecosDto(
                "Rua teste",
                123,
                "Complemento",
                "Cidade Teste",
                "UF Teste",
                "00000-000",
                "Bairro Teste");
        enderecos = new Enderecos(enderecosDto);
        filialDto = new FilialRequestDto(
                "Nome Fantasia ",
                "Razão Social ",
                "12345678901236",
                "9999999999",
                "teste3@Teste.com",
                SituacaoContrato.ATIVO,
                null);
        filial = new Filial(filialDto);
        filial.setId(1L);

        filialRepository.save(filial);

        FilialRequestDto filialDtoAlterada = new FilialRequestDto(
                "Nome Fantasia Alterado",
                "Razão Social Alterada",
                "12345678901237",
                "9999999999",
                "teste3@Teste.com",
                SituacaoContrato.ATIVO,
                enderecosDto);

        when(enderecosValidator.validateEndereco(filialDtoAlterada.endereco())).thenReturn(enderecos);
        when(filialRepository.save(any(Filial.class))).thenReturn(filial);
        when(filialRepository.findById(1L)).thenReturn(Optional.of(filial));

        FilialResponseDto filialAlterada = service.updateFilial(1L, filialDtoAlterada);

        assertNotNull(filialAlterada);
        assertEquals(1L, filialAlterada.id());
        assertEquals(filialDtoAlterada.nomeFantasia(), filialAlterada.nomeFantasia());
        assertEquals(filialDtoAlterada.razaoSocial(), filialAlterada.razaoSocial());
        assertEquals(filialDtoAlterada.cnpj(), filialAlterada.cnpj());
        assertEquals(filialDtoAlterada.telefone(), filialAlterada.telefone());
        assertEquals(filialDtoAlterada.email(), filialAlterada.email());
        assertEquals(filialDtoAlterada.situacaoContrato(), filialAlterada.situacaoContrato());
        assertEquals(filialDtoAlterada.endereco(), filialAlterada.endereco());
        assertDoesNotThrow(() -> {
            cnpjValidator.validateCnpjUpdateFilial(filialDtoAlterada.cnpj(), 1L);
        });

        verify(filialRepository).findById(1L);
        verify(enderecosValidator).validateEndereco(filialDtoAlterada.endereco());
    }

    @Test
    @DisplayName("Teste updateFilial - NotFoundException")
    public void updateFilial_ReturnNotFound() {
        when(filialRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.updateFilial(1L, filialDto);
        });

        assertEquals("Filial não encontrada", exception.getMessage());

        verify(filialRepository, never()).save(any(Filial.class));
        verify(filialRepository).findById(1L);
    }

    @Test
    @DisplayName("Teste getFilialById - retorna filial cadastrada em ResponseDto")
    public void getFilialById_ReturnFilial() {
        enderecosDto = new EnderecosDto(
                "Rua teste",
                123,
                "Complemento",
                "Cidade Teste",
                "UF Teste",
                "00000-000",
                "Bairro Teste");
        enderecos = new Enderecos(enderecosDto);
        filialDto = new FilialRequestDto(
                "Nome Fantasia ",
                "Razão Social ",
                "12345678901236",
                "9999999999",
                "teste3@Teste.com",
                SituacaoContrato.ATIVO,
                null);
        filial = new Filial(filialDto);
        filial.setId(1L);
        filial.setEndereco(enderecos);

        when(filialRepository.findById(1L)).thenReturn(Optional.of(filial));
        FilialResponseDto filialResponseDto = service.getFilialById(1L);

        assertNotNull(filialResponseDto);
        assertEquals(1L, filialResponseDto.id());
        assertEquals(filialDto.nomeFantasia(), filialResponseDto.nomeFantasia());
        assertEquals(filialDto.razaoSocial(), filialResponseDto.razaoSocial());
        assertEquals(filialDto.cnpj(), filialResponseDto.cnpj());
        assertEquals(filialDto.telefone(), filialResponseDto.telefone());
        assertEquals(filialDto.email(), filialResponseDto.email());
        assertEquals(filialDto.situacaoContrato(), filialResponseDto.situacaoContrato());
        assertEquals(enderecosDto, filialResponseDto.endereco());

        verify(filialRepository).findById(1L);
    }

    @Test
    @DisplayName("Teste getFilialById - retorna NotFoundException")
    public void getFilialById_ReturnNotFound() {
        when(filialRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.getFilialById(1L);
        });

        assertEquals("Filial não encontrada", exception.getMessage());

        verify(filialRepository).findById(1L);
    }

    @Test
    @DisplayName("Teste getFilialFilter - retorna lista de filialReponseDto paginada")
    public void getFilialFilter_ReturnFilial() {
        String nomeFiltro = "Nome Teste";
        String cnpjFiltro = "12345678901234";

        Pageable pageable = PageRequest.of(0, 10);

        List<FilialResponseDto> responseDtoList = Arrays.asList(
                new FilialResponseDto(
                        1L,
                        "Filial 1",
                        "Razao1",
                        cnpjFiltro,
                        "12345678910",
                        "filial1@filial1",
                        SituacaoContrato.ATIVO,
                        null),
                new FilialResponseDto(
                        2L,
                        "Filial 2",
                        "Razao2",
                        "09876543211234",
                        "0987654321",
                        "filial2@filial2",
                        SituacaoContrato.ATIVO,
                        null));

        Page<FilialResponseDto> page = new PageImpl<>(responseDtoList, pageable, responseDtoList.size());

        when(filialRepository.buscarFiliais(nomeFiltro, cnpjFiltro, pageable)).thenReturn(page);

        Page<FilialResponseDto> result = service.getFilialFilter(nomeFiltro, cnpjFiltro, pageable);

        assertNotNull(result);

        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(responseDtoList.size(), result.getContent().size());
        assertEquals("Filial 1", result.getContent().get(0).nomeFantasia());
        assertEquals("Filial 2", result.getContent().get(1).nomeFantasia());
    }

    @Test
    @DisplayName("Teste getFilialFilter - Blank")
    public void getFilialFilter_ReturnBlank() {
        String nomeFiltro = "Nome Teste";
        String cnpjFiltro = "12345678901234";

        Pageable pageable = PageRequest.of(0, 10);

        Page<FilialResponseDto> page = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(filialRepository.buscarFiliais(nomeFiltro, cnpjFiltro, pageable)).thenReturn(page);

        Page<FilialResponseDto> result = service.getFilialFilter(nomeFiltro, cnpjFiltro, pageable);

        assertNotNull(result);

        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getContent().size());
    }

    @Test
    @DisplayName("buscaFilialPorId - retorna Optional<Filial>")
    public void buscaFilialPorId_ReturnFilial() {
        enderecosDto = new EnderecosDto(
                "Rua teste",
                123,
                "Complemento",
                "Cidade Teste",
                "UF Teste",
                "00000-000",
                "Bairro Teste");
        enderecos = new Enderecos(enderecosDto);
        filialDto = new FilialRequestDto(
                "Nome Fantasia ",
                "Razão Social ",
                "12345678901236",
                "9999999999",
                "teste3@teste.com",
                SituacaoContrato.ATIVO,
                null);
        filial = new Filial(filialDto);
        filial.setId(1L);
        filial.setEndereco(enderecos);

        when(filialRepository.findById(1L)).thenReturn(Optional.of(filial));

        Optional<Filial> result = service.buscaFilialPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(filial, result.get());

        verify(filialRepository).findById(1L);
        verifyNoMoreInteractions(filialRepository);
        verifyNoMoreInteractions(enderecosValidator);
    }

    @Test
    @DisplayName("buscaFilialPorId - retorna Optional.empty")
    public void buscaFilialPorId_ReturnNotFound() {
        when(filialRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Filial> result = service.buscaFilialPorId(1L);

        assertFalse(result.isPresent());

        verify(filialRepository).findById(1L);
        verifyNoMoreInteractions(filialRepository);
        verifyNoMoreInteractions(enderecosValidator);
    }
}
